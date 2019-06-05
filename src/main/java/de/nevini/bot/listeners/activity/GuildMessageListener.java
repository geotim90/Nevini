package de.nevini.bot.listeners.activity;

import de.nevini.bot.services.common.ActivityService;
import de.nevini.commons.concurrent.EventDispatcher;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildMessageListener {

    private final ActivityService activityService;

    public GuildMessageListener(
            @Autowired ActivityService activityService,
            @Autowired EventDispatcher<Event> eventDispatcher
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
