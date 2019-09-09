package de.nevini.listeners.activity;

import de.nevini.jpa.game.GameData;
import de.nevini.services.common.ActivityService;
import de.nevini.services.common.GameService;
import de.nevini.util.concurrent.EventDispatcher;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityPlayingListener {

    private final ActivityService activityService;
    private final GameService gameService;

    public ActivityPlayingListener(
            @Autowired ActivityService activityService,
            @Autowired GameService gameService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.activityService = activityService;
        this.gameService = gameService;
        eventDispatcher.subscribe(UserActivityStartEvent.class, this::onUserActivityStart);
    }

    private void onUserActivityStart(UserActivityStartEvent e) {
        if (!e.getUser().isBot()) {
            processUserGame(e.getUser(), e.getNewActivity());
        }
    }

    private void processUserGame(User user, Activity activity) {
        RichPresence presence = activity != null ? activity.asRichPresence() : null;
        if (presence != null && presence.getType() == Activity.ActivityType.DEFAULT) {
            GameData game = gameService.getGame(presence);
            if (game != null) {
                activityService.updateActivityPlaying(user, game);
            }
        }
    }

}
