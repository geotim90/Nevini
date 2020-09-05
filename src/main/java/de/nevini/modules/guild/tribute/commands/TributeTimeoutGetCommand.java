package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;

public class TributeTimeoutGetCommand extends Command {

    public TributeTimeoutGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.GUILD_TRIBUTE_TIMEOUT_GET)
                .description("displays the timeout for users that need to contribute")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Long timeout = event.getTributeService().getTimeout(event.getGuild());
        if (timeout == null || timeout < 1) {
            event.reply("No tribute timeout has been configured.", event::complete);
        } else {
            event.reply("The configured tribute timeout is **" + timeout.toString() + " days**", event::complete);
        }
    }

}
