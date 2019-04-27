package de.nevini.commands.guild.activity;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.bot.EventDispatcher;
import de.nevini.commands.guild.GuildCategory;
import de.nevini.services.ActivityService;
import de.nevini.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityCommand extends AbstractCommand {

    protected ActivityCommand(
            @Autowired GuildCategory category,
            @Autowired EventDispatcher eventDispatcher,
            @Autowired ActivityService activityService,
            @Autowired GameService gameService
    ) {
        super("activity", "displays user activity information", category,
                new ActivityUserCommand(eventDispatcher, activityService, gameService),
                new ActivityGameCommand(eventDispatcher, activityService, gameService));
    }

    @Override
    protected void execute(CommandEvent event) {
        children[0].run(event);
    }

}
