package de.nevini.commands.core.prefix;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.CommandComponent;
import de.nevini.commands.core.CoreCategory;
import de.nevini.services.PrefixService;
import net.dv8tion.jda.core.Permission;
import org.springframework.beans.factory.annotation.Autowired;

@CommandComponent
public class PrefixCommand extends Command {

    private final PrefixGetCommand getCommand;

    public PrefixCommand(
            @Autowired CoreCategory coreCategory,
            @Autowired PrefixService prefixService
    ) {
        this.name = "prefix";
        this.help = "configures the bot's prefix";
        this.category = coreCategory;
        this.arguments = "( get | set <prefix> )";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
        this.children = new Command[]{
                getCommand = new PrefixGetCommand(prefixService),
                new PrefixSetCommand(prefixService)
        };
    }

    @Override
    protected void execute(CommandEvent event) {
        // delegate to "get" command
        getCommand.execute(event);
    }

}
