package de.nevini.listeners;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuScore;
import com.oopsjpeg.osu4j.OsuUser;
import de.nevini.db.feed.FeedData;
import de.nevini.scope.Feed;
import de.nevini.services.common.FeedService;
import de.nevini.services.common.IgnService;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.TextChannel;
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
            @Autowired EventDispatcher eventDispatcher
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
                FeedData eventsFeed = feedService.getSubscription(event.getGuild(), Feed.OSU_EVENTS);
                if (eventsFeed != null) {
                    TextChannel channel = event.getGuild().getTextChannelById(eventsFeed.getChannel());
                    if (channel != null) {
                        updateEvents(event, eventsFeed, channel);
                    }
                }
                // update osu! plays for subscribed channels
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

    // synchronized to prevent uts race conditions
    private synchronized void updateEvents(UserUpdateGameEvent event, FeedData feed, TextChannel channel) {
        ZonedDateTime uts = ZonedDateTime.ofInstant(Instant.ofEpochMilli(feed.getUts()), ZoneOffset.UTC);
        Member member = event.getMember();
        String ign = StringUtils.defaultIfEmpty(ignService.getIgn(member, osuService.getGame()), member.getEffectiveName());
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        int days = (int) Math.max(1, Math.min(ChronoUnit.DAYS.between(uts, now), 31));
        log.info("Querying user events for {} days ({} to {})", days, Formatter.formatTimestamp(uts), Formatter.formatTimestamp(now));
        OsuUser user = osuService.getUser(ign, GameMode.STANDARD, days);
        if (user != null) {
            user.getEvents().stream()
                    .filter(e -> e.getDate().isAfter(uts))
                    .sorted(Comparator.comparing(OsuUser.Event::getDate))
                    .forEach(e -> {
                        String markdown = Formatter.formatOsuDisplayHtml(e.getDisplayHTML()) + " at " + Formatter.formatTimestamp(e.getDate());
                        log.info("Feed {} on {} in {}: {}", feed.getType(), channel.getGuild().getId(), channel.getId(), markdown);
                        channel.sendMessage(markdown).queue();
                    });
            user.getEvents().stream()
                    .filter(e -> e.getDate().isAfter(uts))
                    .map(OsuUser.Event::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(channel, Feed.OSU_EVENTS, max.toInstant().toEpochMilli()));
        }
    }

    // synchronized to prevent uts race conditions
    private synchronized void updateRecent(UserUpdateGameEvent event, FeedData feed, TextChannel channel) {
        ZonedDateTime uts = ZonedDateTime.ofInstant(Instant.ofEpochMilli(feed.getUts()), ZoneOffset.UTC);
        Member member = event.getMember();
        String ign = StringUtils.defaultIfEmpty(ignService.getIgn(member, osuService.getGame()), member.getEffectiveName());
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        log.info("Querying user recent ({} to {})", Formatter.formatTimestamp(uts), Formatter.formatTimestamp(now));
        List<OsuScore> scores = osuService.getUserRecent(ign, GameMode.STANDARD);
        if (scores != null && !scores.isEmpty()) {
            int userId = scores.get(0).getUserID();
            String userName = osuService.getUserName(userId);
            scores.stream()
                    // only consider recent beatmap scores that were not a fail or retry
                    .filter(e -> e.getBeatmapID() != 0 && !"F".equals(e.getRank()) && e.getDate().isAfter(uts))
                    .sorted(Comparator.comparing(OsuScore::getDate))
                    .forEach(e -> {
                        String markdown = Formatter.formatRank(e.getRank()) + " " + userName + " achieved " +
                                Formatter.formatInteger(e.getScore()) + " points on "
                                + osuService.getBeatmapTitle(e.getBeatmapID()) + " ["
                                + osuService.getBeatmapVersion(e.getBeatmapID()) + "] ("
                                + osuService.getBeatmapMode(e.getBeatmapID()).getName() + ") at "
                                + Formatter.formatTimestamp(e.getDate());
                        log.info("Feed {} on {} in {}: {}", feed.getType(), channel.getGuild().getId(), channel.getId(), markdown);
                        channel.sendMessage(markdown).queue();
                    });
            scores.stream()
                    .filter(e -> e.getDate().isAfter(uts))
                    .map(OsuScore::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(channel, Feed.OSU_RECENT, max.toInstant().toEpochMilli()));
        }
    }

}
