package de.nevini.listeners.activity;

import de.nevini.services.common.ActivityService;
import de.nevini.util.concurrent.EventDispatcher;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityOnlineListener {

    private final ActivityService activityService;

    public ActivityOnlineListener(
            @Autowired ActivityService activityService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.activityService = activityService;
        eventDispatcher.subscribe(UserUpdateOnlineStatusEvent.class, this::onUserUpdateOnlineStatus);
    }

    private void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        if (!event.getUser().isBot() && (event.getNewOnlineStatus() == OnlineStatus.ONLINE
                || event.getNewOnlineStatus() == OnlineStatus.IDLE
                || event.getNewOnlineStatus() == OnlineStatus.DO_NOT_DISTURB)
        ) {
            activityService.updateActivityOnline(event.getUser());
        }
    }
}
