package de.nevini.commands.admin.shutdown;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.bot.CommandComponent;
import de.nevini.commands.admin.AdminCategory;
import org.springframework.beans.factory.annotation.Autowired;

@CommandComponent
public class ShutdownCommand extends AbstractCommand {

    public ShutdownCommand(@Autowired AdminCategory category) {
        super("shutdown", "safely shuts down the bot", category);
        this.name = "shutdown";
        this.guildOnly = false;
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reactSuccess();
        event.getJDA().shutdown();
    }

}
