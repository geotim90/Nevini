package de.nevini.listeners;

import de.nevini.services.common.ActivityService;
import de.nevini.services.common.GameService;
import de.nevini.services.common.IgnService;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GameListener {

    private final ActivityService activityService;
    private final GameService gameService;
    private final IgnService ignService;

    public GameListener(
            @Autowired ActivityService activityService,
            @Autowired GameService gameService,
            @Autowired IgnService ignService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.activityService = activityService;
        this.gameService = gameService;
        this.ignService = ignService;
        eventDispatcher.subscribe(UserUpdateGameEvent.class, this::onUserUpdateGame);
    }

    private void onUserUpdateGame(UserUpdateGameEvent e) {
        if (!e.getUser().isBot()) {
            processUserGame(e.getUser(), e.getOldGame());
            processUserGame(e.getUser(), e.getNewGame());
        }
    }

    private void processUserGame(User user, Game game) {
        RichPresence presence = game != null ? game.asRichPresence() : null;
        if (presence != null && presence.getType() == Game.GameType.DEFAULT) {
            activityService.updateActivityPlaying(user, presence);
            gameService.cacheGame(presence);
            checkForIgns(user, presence);
        }
    }

    private void checkForIgns(User user, RichPresence presence) {
        // osu! presence contains the in-game name
        if ("osu!".equals(presence.getName()) && presence.getLargeImage() != null) {
            String text = ObjectUtils.defaultIfNull(presence.getLargeImage().getText(), "");
            Matcher matcher = Pattern.compile("(.+) \\(rank #[\\d,]+\\)").matcher(text);
            if (matcher.matches()) {
                ignService.setIgn(user, presence.getApplicationIdLong(), matcher.group(1));
            }
        }
    }

}
