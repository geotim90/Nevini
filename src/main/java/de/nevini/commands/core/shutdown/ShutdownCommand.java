package de.nevini.commands.core.shutdown;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.CommandComponent;

@CommandComponent
public class ShutdownCommand extends Command {

    public ShutdownCommand() {
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
