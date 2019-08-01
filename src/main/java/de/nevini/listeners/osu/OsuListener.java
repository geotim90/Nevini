package de.nevini.listeners.osu;

import de.nevini.api.osu.model.*;
import de.nevini.jpa.feed.FeedData;
import de.nevini.jpa.ign.IgnData;
import de.nevini.scope.Feed;
import de.nevini.services.common.FeedService;
import de.nevini.services.common.IgnService;
import de.nevini.services.osu.OsuService;
import de.nevini.util.Formatter;
import de.nevini.util.concurrent.EventDispatcher;
import de.nevini.util.message.MessageLineSplitter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
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
import java.util.stream.Collectors;

import static de.nevini.util.Functions.ignore;

@Slf4j
@Component
public class OsuListener {

    private final IgnService ignService;
    private final FeedService feedService;
    private final OsuService osuService;

    private final EventDispatcher eventDispatcher;
    private final Set<Long> updateQueue = new LinkedHashSet<>();
    private final Map<Long, Long> its = new ConcurrentHashMap<>();
    private long uts = 0L;

    public OsuListener(
            @Autowired IgnService ignService,
            @Autowired FeedService feedService,
            @Autowired OsuService osuService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.ignService = ignService;
        this.feedService = feedService;
        this.osuService = osuService;

        this.eventDispatcher = eventDispatcher;
        eventDispatcher.subscribe(UserActivityStartEvent.class, this::onUserActivityStart);
    }

    private void onUserActivityStart(UserActivityStartEvent e) {
        if (!e.getUser().isBot()) {
            processUserActivity(e, e.getNewActivity());
            updateQueue(e.getJDA());
        }
    }

    private void processUserActivity(UserActivityStartEvent event, Activity activity) {
        if (osuService.getGame().getName().equals(activity.getName())) {
            // osu! presence contains the in-game name
            RichPresence presence = activity.asRichPresence();
            if (presence != null && presence.getLargeImage() != null) {
                String text = ObjectUtils.defaultIfNull(presence.getLargeImage().getText(), "");
                Matcher matcher = Pattern.compile("(.+) \\(rank #[\\d,]+\\)").matcher(text);
                if (matcher.matches()) {
                    ignService.setIgn(event.getUser(), presence.getApplicationIdLong(), matcher.group(1));
                }
            }

            // update queue if a subscription exists
            for (Guild guild : event.getJDA().getGuilds()) {
                Member member = guild.getMember(event.getUser());
                if (member != null) {
                    FeedData feedEvents = feedService.getSubscription(Feed.OSU_EVENTS, guild);
                    FeedData feedRecent = feedService.getSubscription(Feed.OSU_RECENT, guild);
                    if (feedEvents != null || feedRecent != null) {
                        log.debug("Queueing {}", event.getUser().getId());
                        updateQueue.add(event.getUser().getIdLong());
                        break;
                    }
                }
            }
        }
    }

    private synchronized void updateQueue(JDA jda) {
        // only execute once every minute at maximum
        long now = System.currentTimeMillis();
        if (now - uts < TimeUnit.MINUTES.toMillis(1)) return;

        // iterate over guilds
        for (Guild guild : jda.getGuilds()) {
            // only include guilds with feed subscriptions
            FeedData feedEvents = feedService.getSubscription(Feed.OSU_EVENTS, guild);
            FeedData feedRecent = feedService.getSubscription(Feed.OSU_RECENT, guild);
            if (feedEvents != null || feedRecent != null) {
                // only include members with known in-game names
                for (IgnData ign : ignService.getIgns(guild, osuService.getGame())) {
                    if (guild.getMemberById(ign.getUser()) != null) {
                        // only auto-queue updates once every hour per user
                        if (now - its.getOrDefault(ign.getUser(), 0L) >= TimeUnit.HOURS.toMillis(1)) {
                            log.debug("Auto-queueing {}", ign.getUser());
                            updateQueue.add(ign.getUser());
                            its.put(ign.getUser(), now);
                        }
                    }
                }
            }
        }

        // process up to 6 users per minute
        int tokens = 6;
        Iterator<Long> iterator = updateQueue.iterator();
        while (iterator.hasNext() && tokens-- > 0) {
            long user = iterator.next();
            log.debug("Processing {}", user);
            eventDispatcher.execute(() -> updateUser(jda, user));
            iterator.remove();
        }
        if (!updateQueue.isEmpty()) {
            log.debug("{} queued users remaining", updateQueue.size());
        }

        // update timestamp
        uts = now;
    }

    private void updateUser(JDA jda, Long user) {
        try {
            updateEvents(jda, user);
            updateRecent(jda, user);
        } catch (RuntimeException e) {
            log.warn("Exception: ", e);
        }
    }

    private synchronized void updateEvents(JDA jda, Long userId) {
        // get in-game names
        List<String> igns = getInGameNames(userId);
        if (igns.isEmpty()) return;

        // get feed subscriptions
        List<FeedData> feeds = getFeeds(Feed.OSU_EVENTS, jda, userId);
        if (feeds.isEmpty()) return;

        // get min uts from feeds
        long minUts = feeds.stream().map(FeedData::getUts).min(Comparator.naturalOrder()).orElse(0L);

        for (String ign : igns) {
            // calculate required number of days for query
            ZonedDateTime then = ZonedDateTime.ofInstant(Instant.ofEpochMilli(minUts), ZoneOffset.UTC);
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            int days = (int) Math.max(1, Math.min(ChronoUnit.DAYS.between(then, now), 31));
            log.debug(
                    "Querying user events for {} days ({} to {})",
                    days,
                    Formatter.formatTimestamp(minUts),
                    Formatter.formatTimestamp(now)
            );

            // query data
            OsuUser user = osuService.getUserEvents(ign, OsuMode.STANDARD, days);
            if (user != null && !user.getEvents().isEmpty()) {
                for (FeedData feed : feeds) {
                    // get guild
                    Guild guild = jda.getGuildById(feed.getGuild());
                    if (guild == null) continue;

                    // get member
                    Member member = guild.getMemberById(userId);
                    if (member == null) continue;

                    // get channel
                    TextChannel channel = guild.getTextChannelById(feed.getChannel());
                    if (channel == null) continue;

                    // collect events
                    StringBuilder builder = new StringBuilder();
                    user.getEvents().stream()
                            .filter(e -> e.getDate().getTime() > feed.getUts())
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
                            .filter(e -> e.getDate().getTime() > feed.getUts())
                            .map(OsuUserEvent::getDate)
                            .max(Comparator.naturalOrder())
                            .ifPresent(max -> feedService.updateSubscription(
                                    Feed.OSU_EVENTS, member.getUser().getIdLong(), channel, max.getTime()
                            ));
                }
            }
        }
    }

    private synchronized void updateRecent(JDA jda, Long userId) {
        // get in-game names
        List<String> igns = getInGameNames(userId);
        if (igns.isEmpty()) return;

        // get feed subscriptions
        List<FeedData> feeds = getFeeds(Feed.OSU_RECENT, jda, userId);
        if (feeds.isEmpty()) return;

        // get min uts from feeds
        long minUts = feeds.stream().map(FeedData::getUts).min(Comparator.naturalOrder()).orElse(0L);

        for (String ign : igns) {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            log.debug(
                    "Querying user recent ({} to {})",
                    Formatter.formatTimestamp(minUts),
                    Formatter.formatTimestamp(now)
            );

            // query data
            List<OsuUserRecent> recent = osuService.getUserRecent(ign, OsuMode.STANDARD);
            if (recent != null && !recent.isEmpty()) {
                for (FeedData feed : feeds) {
                    // get guild
                    Guild guild = jda.getGuildById(feed.getGuild());
                    if (guild == null) continue;

                    // get member
                    Member member = guild.getMemberById(userId);
                    if (member == null) continue;

                    // get channel
                    TextChannel channel = guild.getTextChannelById(feed.getChannel());
                    if (channel == null) continue;

                    // collect recents
                    String userName = osuService.getUserName(recent.get(0).getUserId());
                    StringBuilder builder = new StringBuilder();
                    recent.stream()
                            // only consider recent beatmap scores that were not a fail or retry
                            .filter(e -> e.getBeatmapId() != 0 && !OsuRank.F.equals(e.getRank())
                                    && e.getDate().getTime() > feed.getUts())
                            .sorted(Comparator.comparing(OsuUserRecent::getDate))
                            .forEach(e -> {
                                String markdown = Formatter.formatOsuRank(e.getRank()) + " " + userName + " achieved "
                                        + Formatter.formatInteger(e.getScore()) + " points on "
                                        + osuService.getBeatmapString(e.getBeatmapId()) + " at "
                                        + Formatter.formatTimestamp(e.getDate().getTime());
                                log.debug("Feed {} on {} in {}: {}",
                                        feed.getType(),
                                        channel.getGuild().getId(),
                                        channel.getId(),
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
                            .filter(e -> e.getDate().getTime() > feed.getUts())
                            .map(OsuUserRecent::getDate)
                            .max(Comparator.naturalOrder())
                            .ifPresent(max -> feedService.updateSubscription(
                                    Feed.OSU_RECENT, member.getUser().getIdLong(), channel, max.getTime()
                            ));
                }
            }
        }
    }

    private @NotNull List<String> getInGameNames(long user) {
        return ignService.getIgns(user, osuService.getGame()).stream()
                .map(IgnData::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    private @NotNull List<FeedData> getFeeds(@NotNull Feed type, @NotNull JDA jda, long user) {
        ArrayList<FeedData> feeds = new ArrayList<>();
        for (Guild guild : jda.getGuilds()) {
            FeedData feed = feedService.getSubscription(type, guild, user);
            if (feed != null) {
                feeds.add(feed);
            }
        }
        return feeds;
    }

}
