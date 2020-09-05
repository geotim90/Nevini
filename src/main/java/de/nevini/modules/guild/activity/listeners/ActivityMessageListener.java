package de.nevini.modules.guild.activity.listeners;

import de.nevini.modules.guild.activity.services.ActivityService;
import de.nevini.util.concurrent.EventDispatcher;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityMessageListener {

    private final ActivityService activityService;

    public ActivityMessageListener(
            @Autowired ActivityService activityService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.activityService = activityService;
        eventDispatcher.subscribe(GuildMessageReceivedEvent.class, this::onGuildMessageReceived);
    }

    private void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            activityService.updateActivityMessage(event.getMessage());
            activityService.updateActivityOnline(event.getAuthor());
        }
    }

}
