package de.nevini.listeners;

import de.nevini.bot.EventDispatcher;
import de.nevini.services.ActivityService;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildMessageListener {

    private final ActivityService activityService;

    public GuildMessageListener(
            @Autowired ActivityService activityService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.activityService = activityService;
        eventDispatcher.addEventListener(GuildMessageReceivedEvent.class, this::onGuildMessageReceived);
    }

    private void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            activityService.updateActivityMessage(event.getMessage());
            activityService.updateActivityOnline(event.getAuthor());
        }
    }

}