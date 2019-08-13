package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
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
