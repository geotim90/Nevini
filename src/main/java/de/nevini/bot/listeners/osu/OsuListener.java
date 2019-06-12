package de.nevini.bot.listeners.osu;

import de.nevini.api.osu.model.*;
import de.nevini.bot.db.feed.FeedData;
import de.nevini.bot.db.ign.IgnData;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.services.common.FeedService;
import de.nevini.bot.services.common.IgnService;
import de.nevini.bot.services.osu.OsuService;
import de.nevini.bot.util.Formatter;
import de.nevini.commons.concurrent.EventDispatcher;
import de.nevini.commons.concurrent.TokenBucket;
import de.nevini.framework.message.MessageLineSplitter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.nevini.commons.util.Functions.ignore;

@Slf4j
@Component
public class OsuListener {

    private final IgnService ignService;
    private final FeedService feedService;
    private final OsuService osuService;

    private final EventDispatcher<Event> eventDispatcher;
    private final TokenBucket rateLimiter = new TokenBucket(3, 6, TimeUnit.MINUTES);
    private final Set<IgnData> updateQueue = new LinkedHashSet<>();
    private final Map<IgnData, Long> its = new ConcurrentHashMap<>();
    private long uts = 0L;

    public OsuListener(
            @Autowired IgnService ignService,
            @Autowired FeedService feedService,
            @Autowired OsuService osuService,
            @Autowired EventDispatcher<Event> eventDispatcher
    ) {
        this.ignService = ignService;
        this.feedService = feedService;
        this.osuService = osuService;

        this.eventDispatcher = eventDispatcher;
        eventDispatcher.subscribe(UserUpdateGameEvent.class, this::onUserUpdateGame);
    }

    private void onUserUpdateGame(UserUpdateGameEvent e) {
        if (!e.getUser().isBot()) {
            processUserGame(e, e.getOldGame());
            processUserGame(e, e.getNewGame());
            updateQueue(e.getJDA());
        }
    }

    private void processUserGame(UserUpdateGameEvent event, Game game) {
        if (game != null && game.isRich() && osuService.getGame().getName().equals(game.getName())) {
            // osu! presence contains the in-game name
            RichPresence presence = game.asRichPresence();
            if (presence.getLargeImage() != null) {
                String text = ObjectUtils.defaultIfNull(presence.getLargeImage().getText(), "");
                Matcher matcher = Pattern.compile("(.+) \\(rank #[\\d,]+\\)").matcher(text);
                if (matcher.matches()) {
                    ignService.setIgn(event.getUser(), presence.getApplicationIdLong(), matcher.group(1));
                }
            }

            // update osu! feeds
            IgnData ign = ignService.getIgn(event.getMember(), osuService.getGame());
            if (ign != null) {
                if (rateLimiter.requestToken()) {
                    log.debug("Processing now {}", ign);
                    eventDispatcher.execute(() -> updateMember(event.getJDA(), ign));
                } else {
                    log.debug("Queueing {}", ign);
                    synchronized (updateQueue) {
                        updateQueue.add(ign);
                    }
                }
            }
        }
    }

    private void updateQueue(JDA jda) {
        synchronized (updateQueue) {
            // only execute once every minute at maximum
            long now = System.currentTimeMillis();
            if (now - uts < TimeUnit.MINUTES.toMillis(1)) return;

            for (Guild guild : jda.getGuilds()) {
                // only include guilds with feed subscriptions
                FeedData feedEvents = feedService.getSubscription(Feed.OSU_EVENTS, guild);
                FeedData feedRecent = feedService.getSubscription(Feed.OSU_RECENT, guild);
                if (feedEvents != null || feedRecent != null) {
                    // only include members with known in-game names
                    for (IgnData ign : ignService.getIgns(guild, osuService.getGame())) {
                        if (guild.getMemberById(ign.getUser()) != null) {
                            IgnData guildIgn = new IgnData(
                                    guild.getIdLong(), ign.getUser(), ign.getGame(), ign.getName()
                            );
                            // only auto-queue updates once every hour per guild and ign
                            if (now - its.getOrDefault(guildIgn, 0L) >= TimeUnit.HOURS.toMillis(1)) {
                                log.debug("Auto-queueing {}", guildIgn);
                                updateQueue.add(guildIgn);
                                its.put(guildIgn, now);
                            }
                        }
                    }
                }
            }

            // process as many igns as the rate limiter allows
            Iterator<IgnData> iterator = updateQueue.iterator();
            while (iterator.hasNext() && rateLimiter.requestToken()) {
                IgnData ign = iterator.next();
                log.debug("Processing {}", ign);
                eventDispatcher.execute(() -> updateMember(jda, ign));
                iterator.remove();
            }

            // update timestamp
            uts = now;
        }
    }

    private void updateMember(JDA jda, IgnData ign) {
        try {
            Guild guild = jda.getGuildById(ign.getGuild());
            if (guild != null) {
                Member member = guild.getMemberById(ign.getUser());
                if (member != null) {
                    updateEvents(member);
                    updateRecent(member);
                }
            }
        } catch (RuntimeException e) {
            log.warn("Exception: ", e);
        }
    }

    private synchronized void updateEvents(Member member) {
        // get feed
        FeedData feed = feedService.getSubscription(Feed.OSU_EVENTS, member.getGuild(), member.getUser().getIdLong());
        if (feed == null) return;
        // get channel
        TextChannel channel = member.getGuild().getTextChannelById(feed.getChannel());
        if (channel == null) return;
        // get ign
        String ign = StringUtils.defaultIfEmpty(ignService.getInGameName(member, osuService.getGame()),
                member.getEffectiveName());
        long uts = feed.getUts();
        ZonedDateTime then = ZonedDateTime.ofInstant(Instant.ofEpochMilli(uts), ZoneOffset.UTC);
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        int days = (int) Math.max(1, Math.min(ChronoUnit.DAYS.between(then, now), 31));
        log.debug(
                "Querying user events for {} days ({} to {})",
                days,
                Formatter.formatTimestamp(uts),
                Formatter.formatTimestamp(now)
        );
        OsuUser user = osuService.getUser(ign, OsuMode.STANDARD, days);
        if (user != null && !user.getEvents().isEmpty()) {
            // collect events
            StringBuilder builder = new StringBuilder();
            user.getEvents().stream()
                    .filter(e -> e.getDate().getTime() > uts)
                    .sorted(Comparator.comparing(OsuUserEvent::getDate))
                    .forEach(e -> {
                        String markdown = Formatter.formatOsuDisplayHtml(e.getDisplayHtml())
                                + " at " + Formatter.formatTimestamp(e.getDate().getTime());
                        log.debug("Feed {} on {} in {}: {}", feed.getType(), channel.getGuild().getId(),
                                channel.getId(), markdown);
                        builder.append(markdown).append('\n');
                    });

            // build and send message(s)
            String content = builder.toString();
            if (!StringUtils.isEmpty(content)) {
                MessageLineSplitter.sendMessage(channel, content, ignore());
            }

            // update timestamp
            user.getEvents().stream()
                    .filter(e -> e.getDate().getTime() > uts)
                    .map(OsuUserEvent::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(
                            Feed.OSU_EVENTS, member.getUser().getIdLong(), channel, max.getTime()
                    ));
        }
    }

    private synchronized void updateRecent(Member member) {
        // get feed
        FeedData feed = feedService.getSubscription(Feed.OSU_RECENT, member.getGuild(), member.getUser().getIdLong());
        if (feed == null) return;
        // get channel
        TextChannel channel = member.getGuild().getTextChannelById(feed.getChannel());
        if (channel == null) return;
        // get ign
        String ign = StringUtils.defaultIfEmpty(
                ignService.getInGameName(member, osuService.getGame()), member.getEffectiveName());
        long uts = feed.getUts();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        log.debug("Querying user recent ({} to {})", Formatter.formatTimestamp(uts), Formatter.formatTimestamp(now));
        List<OsuUserRecent> recent = osuService.getUserRecent(ign, OsuMode.STANDARD);
        if (recent != null && !recent.isEmpty()) {
            // collect recents
            int userId = recent.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            StringBuilder builder = new StringBuilder();
            recent.stream()
                    // only consider recent beatmap scores that were not a fail or retry
                    .filter(e -> e.getBeatmapId() != 0 && !OsuRank.F.equals(e.getRank()) && e.getDate().getTime() > uts)
                    .sorted(Comparator.comparing(OsuUserRecent::getDate))
                    .forEach(e -> {
                        String markdown = Formatter.formatOsuRank(e.getRank()) + " " + userName + " achieved " +
                                Formatter.formatInteger(e.getScore()) + " points on "
                                + osuService.getBeatmapString(e.getBeatmapId()) + " at "
                                + Formatter.formatTimestamp(e.getDate().getTime());
                        log.debug("Feed {} on {} in {}: {}", feed.getType(), channel.getGuild().getId(), channel.getId(),
                                markdown);
                        builder.append(markdown).append('\n');
                    });

            // build and send message(s)
            String content = builder.toString();
            if (StringUtils.isNotEmpty(content)) {
                MessageLineSplitter.sendMessage(channel, content, ignore());
            }

            // update timestamp
            recent.stream()
                    .filter(e -> e.getDate().getTime() > uts)
                    .map(OsuUserRecent::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(
                            Feed.OSU_RECENT, member.getUser().getIdLong(), channel, max.getTime()
                    ));
        }
    }

}
