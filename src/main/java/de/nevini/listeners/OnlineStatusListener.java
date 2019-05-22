package de.nevini.listeners;

import de.nevini.services.common.ActivityService;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnlineStatusListener {

    private final ActivityService activityService;

    public OnlineStatusListener(
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
