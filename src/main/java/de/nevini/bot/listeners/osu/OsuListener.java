package de.nevini.bot.listeners.osu;

import de.nevini.api.osu.model.*;
import de.nevini.bot.db.feed.FeedData;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.services.common.FeedService;
import de.nevini.bot.services.common.IgnService;
import de.nevini.bot.services.osu.OsuService;
import de.nevini.bot.util.Formatter;
import de.nevini.commons.concurrent.EventDispatcher;
import de.nevini.framework.message.MessageLineSplitter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.TextChannel;
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
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.nevini.commons.util.Functions.ignore;

@Slf4j
@Component
public class OsuListener {

    private final IgnService ignService;
    private final FeedService feedService;
    private final OsuService osuService;

    public OsuListener(
            @Autowired IgnService ignService,
            @Autowired FeedService feedService,
            @Autowired OsuService osuService,
            @Autowired EventDispatcher<Event> eventDispatcher
    ) {
        this.ignService = ignService;
        this.feedService = feedService;
        this.osuService = osuService;
        eventDispatcher.subscribe(UserUpdateGameEvent.class, this::onUserUpdateGame);
    }

    private void onUserUpdateGame(UserUpdateGameEvent e) {
        if (!e.getUser().isBot()) {
            processUserGame(e, e.getOldGame());
            processUserGame(e, e.getNewGame());
        }
    }

    private void processUserGame(UserUpdateGameEvent event, Game game) {
        if (game != null && game.isRich() && "osu!".equals(game.getName())) {
            RichPresence presence = game.asRichPresence();
            // osu! presence contains the in-game name
            if (presence.getLargeImage() != null) {
                String text = ObjectUtils.defaultIfNull(presence.getLargeImage().getText(), "");
                Matcher matcher = Pattern.compile("(.+) \\(rank #[\\d,]+\\)").matcher(text);
                if (matcher.matches()) {
                    ignService.setIgn(event.getUser(), presence.getApplicationIdLong(), matcher.group(1));
                }
            }
            if (event.getGuild() != null) {
                // update osu! events for subscribed channels
                synchronized (this) {
                    FeedData eventsFeed = feedService.getSubscription(event.getGuild(), Feed.OSU_EVENTS);
                    if (eventsFeed != null) {
                        TextChannel channel = event.getGuild().getTextChannelById(eventsFeed.getChannel());
                        if (channel != null) {
                            updateEvents(event, eventsFeed, channel);
                        }
                    }
                }
                // update osu! plays for subscribed channels
                synchronized (this) {
                    FeedData recentFeed = feedService.getSubscription(event.getGuild(), Feed.OSU_RECENT);
                    if (recentFeed != null) {
                        TextChannel channel = event.getGuild().getTextChannelById(recentFeed.getChannel());
                        if (channel != null) {
                            updateRecent(event, recentFeed, channel);
                        }
                    }
                }
            }
        }
    }

    private void updateEvents(UserUpdateGameEvent event, FeedData feed, TextChannel channel) {
        Member member = event.getMember();
        String ign = StringUtils.defaultIfEmpty(ignService.getIgn(member, osuService.getGame()),
                member.getEffectiveName());
        long uts = feed.getUts();
        ZonedDateTime then = ZonedDateTime.ofInstant(Instant.ofEpochMilli(uts), ZoneOffset.UTC);
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        int days = (int) Math.max(1, Math.min(ChronoUnit.DAYS.between(then, now), 31));
        log.info(
                "Querying user events for {} days ({} to {})",
                days,
                Formatter.formatTimestamp(uts),
                Formatter.formatTimestamp(now)
        );
        OsuUser user = osuService.getUser(ign, OsuMode.STANDARD, days);
        if (user != null) {
            StringBuilder builder = new StringBuilder();
            user.getEvents().stream()
                    .filter(e -> e.getDate().getTime() > uts)
                    .sorted(Comparator.comparing(OsuUserEvent::getDate))
                    .forEach(e -> {
                        String markdown = Formatter.formatOsuDisplayHtml(e.getDisplayHtml())
                                + " at " + Formatter.formatTimestamp(e.getDate().getTime());
                        log.info("Feed {} on {} in {}: {}", feed.getType(), channel.getGuild().getId(),
                                channel.getId(), markdown);
                        builder.append(markdown).append('\n');
                    });
            MessageLineSplitter.sendMessage(channel, builder.toString(), ignore());
            user.getEvents().stream()
                    .filter(e -> e.getDate().getTime() > uts)
                    .map(OsuUserEvent::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(channel, Feed.OSU_EVENTS, max.getTime()));
        }
    }

    private void updateRecent(UserUpdateGameEvent event, FeedData feed, TextChannel channel) {
        Member member = event.getMember();
        String ign = StringUtils.defaultIfEmpty(
                ignService.getIgn(member, osuService.getGame()), member.getEffectiveName());
        long uts = feed.getUts();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        log.info("Querying user recent ({} to {})", Formatter.formatTimestamp(uts), Formatter.formatTimestamp(now));
        List<OsuUserRecent> recent = osuService.getUserRecent(ign, OsuMode.STANDARD);
        if (recent != null && !recent.isEmpty()) {
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
                        log.info("Feed {} on {} in {}: {}", feed.getType(), channel.getGuild().getId(), channel.getId(),
                                markdown);
                        builder.append(markdown).append('\n');
                    });
            MessageLineSplitter.sendMessage(channel, builder.toString(), ignore());
            recent.stream()
                    .filter(e -> e.getDate().getTime() > uts)
                    .map(OsuUserRecent::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(channel, Feed.OSU_RECENT, max.getTime()));
        }
    }

}
