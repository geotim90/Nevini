package de.nevini.listeners;

import de.nevini.bot.EventDispatcher;
import de.nevini.services.GameService;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.RichPresence;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class GameListener {

    private final Set<Long> idCache = new HashSet<>();

    private final GameService gameService;

    public GameListener(
            @Autowired GameService gameService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.gameService = gameService;
        eventDispatcher.addEventListener(UserUpdateGameEvent.class, this::onUserUpdateGame);
    }

    private void onUserUpdateGame(UserUpdateGameEvent e) {
        if (!e.getUser().isBot()) {
            processGame(e.getOldGame());
            processGame(e.getNewGame());
        }
    }

    private void processGame(Game game) {
        RichPresence presence = game != null ? game.asRichPresence() : null;
        if (presence != null && presence.getType() == Game.GameType.DEFAULT) {
            if (idCache.add(presence.getApplicationIdLong())) {
                gameService.putGame(presence);
            }
        }
    }

}
