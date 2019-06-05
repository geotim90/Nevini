package de.nevini.bot.listeners.activity;

import de.nevini.bot.services.common.ActivityService;
import de.nevini.commons.concurrent.EventDispatcher;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnlineStatusListener {

    private final ActivityService activityService;

    public OnlineStatusListener(
            @Autowired ActivityService activityService,
            @Autowired EventDispatcher<Event> eventDispatcher
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
