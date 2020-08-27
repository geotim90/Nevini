package de.nevini.modules.geobot.role;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Role;

class AddRoleMemberCommand extends Command {

    AddRoleMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .node(Node.GEOBOT_ADMIN)
                .description("configures the roles for users to include in inactivity reports")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.ROLE.describe(false, true)
                })
                .details("This will affect the server level role permission overrides for node **geobot.user**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptRole(event, role));
    }

    private void acceptRole(CommandEvent event, Role role) {
        event.getPermissionService().setRolePermission(role, Node.GEOBOT_USER, Boolean.TRUE);
        event.reply(CommandReaction.OK, event::complete);
    }

}
