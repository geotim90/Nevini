package de.nevini.commands.core.prefix;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.nevini.bot.AbstractCommand;
import de.nevini.commands.core.CoreCategory;
import de.nevini.services.PrefixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrefixCommand extends AbstractCommand {

    public PrefixCommand(
            @Autowired CoreCategory category,
            @Autowired PrefixService prefixService
    ) {
        super("prefix", "configures the bot's prefix", category,
                new PrefixGetCommand(prefixService),
                new PrefixSetCommand(prefixService));
    }

    @Override
    protected void execute(CommandEvent event) {
        children[0].run(event);
    }

}
