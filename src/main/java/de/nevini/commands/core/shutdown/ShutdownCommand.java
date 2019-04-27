package de.nevini.commands.core.shutdown;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.core.CoreCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShutdownCommand extends AbstractCommand {

    public ShutdownCommand(@Autowired CoreCategory category) {
        super("shutdown", "safely shuts down the bot", category);
        this.name = "shutdown";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reactSuccess();
        event.getJDA().shutdown();
    }

}
