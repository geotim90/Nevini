package de.nevini.listeners;

import com.oopsjpeg.osu4j.GameMode;
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
            // update user events for subscribed channels
            if (event.getGuild() != null) {
                FeedData feed = feedService.getSubscription(event.getGuild(), Feed.OSU_EVENTS);
                if (feed != null) {
                    TextChannel channel = event.getGuild().getTextChannelById(feed.getChannel());
                    if (channel != null) {
                        updateEvents(event, feed, channel);
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
                        String markdown = convertHtmlToMarkdown(e.getDisplayHTML());
                        log.info("Feed {} on {} in {} at {}: {}", feed.getType(), channel.getGuild().getId(), channel.getId(), Formatter.formatTimestamp(e.getDate()), markdown);
                        channel.sendMessage(markdown).queue();
                    });
            user.getEvents().stream()
                    .filter(e -> e.getDate().isAfter(uts))
                    .map(OsuUser.Event::getDate)
                    .max(Comparator.naturalOrder())
                    .ifPresent(max -> feedService.updateSubscription(channel, Feed.OSU_EVENTS, max.toInstant().toEpochMilli()));
        }
    }

    private String convertHtmlToMarkdown(String html) {
        return html.replaceAll("<img src='/images/(\\w+)_small.png'/>", "**$1**") // resolve rank images
                .replaceAll("<b><a href='(/u/\\d+)'>([^<]+)</a></b>", "$2") // resolve user references
                .replaceAll("<a href='(/b/\\d+\\?m=\\d)'>([^<]+)</a>", "$2") // resolve beatmap references
                // resolve HTML formatting
                .replaceAll("<b>([^<]+)</b>", "**$1**") // bold text emphasis
                // resolve HTML entities
                .replaceAll("&amp;", "&")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<");
    }

}
