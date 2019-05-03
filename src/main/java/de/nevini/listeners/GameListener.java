package de.nevini.listeners;

import de.nevini.services.ActivityService;
import de.nevini.services.GameService;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameListener {

    private final ActivityService activityService;
    private final GameService gameService;

    public GameListener(
            @Autowired ActivityService activityService,
            @Autowired GameService gameService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.activityService = activityService;
        this.gameService = gameService;
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
        }
    }

}
