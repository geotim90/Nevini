package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Role;

class RemoveRoleModCommand extends Command {

    RemoveRoleModCommand() {
        super(CommandDescriptor.builder()
                .keyword("mod")
                .aliases(new String[]{"moderator"})
                .node(Node.GEOBOT_ADMIN)
                .description("configures server level role permission overrides for node **geobot.mod**")
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
        event.getPermissionService().setRolePermission(role, Node.GEOBOT_MOD, null);
        event.reply(CommandReaction.OK, event::complete);
    }

}
