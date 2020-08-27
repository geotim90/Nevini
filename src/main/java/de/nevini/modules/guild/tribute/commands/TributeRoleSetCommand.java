package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Role;

public class TributeRoleSetCommand extends Command {

    public TributeRoleSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_TRIBUTE_ROLE_SET)
                .description("configures the role for users that need to contribute")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.ROLE.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptRole(event, role));
    }

    private void acceptRole(CommandEvent event, Role role) {
        event.getTributeService().setRole(role);
        event.reply(CommandReaction.OK, event::complete);
    }

}
