package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandReaction;

class PrefixUnsetCommand extends Command {

    PrefixUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .node(Node.CORE_PREFIX_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.TALK, Permissions.REACT))
                .description("removes the configured command prefix")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getPrefixService().removeGuildPrefix(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
