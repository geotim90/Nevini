package de.nevini.modules.core.prefix.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;

class PrefixGetCommand extends Command {

    PrefixGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .node(Node.CORE_PREFIX_GET)
                .minimumBotPermissions(Permissions.TALK)
                .description("displays the currently configured command prefix")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(event.getPrefixService().getGuildPrefix(event.getGuild()), event::complete);
    }

}
