package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;

class TributeTimeoutGetCommand extends Command {

    TributeTimeoutGetCommand() {
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
