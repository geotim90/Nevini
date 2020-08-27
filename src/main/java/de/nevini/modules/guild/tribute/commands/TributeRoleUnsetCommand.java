package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandReaction;

public class TributeRoleUnsetCommand extends Command {

    public TributeRoleUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove"})
                .node(Node.GUILD_TRIBUTE_ROLE_SET)
                .description("removes the configured role for users that need to contribute")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getTributeService().unsetRole(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
