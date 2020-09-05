package de.nevini.modules.geobot.role;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Role;

class AddRoleModCommand extends Command {

    AddRoleModCommand() {
        super(CommandDescriptor.builder()
                .keyword("mod")
                .aliases(new String[]{"moderator"})
                .node(Node.GEOBOT_ADMIN)
                .description("configures server level role permission overrides for node **geobot.mod**")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.ROLE.describe(false, true)
                })
                .details("This command also configures server level role permission overrides for the following nodes:"
                        + "\n* **guild.activity.get**"
                        + "\n* **guild.inactivity.get**"
                        + "\n* **guild.tribute.get**"
                        + "\n* **guild.tribute.set**"
                        + "\n* **guild.tribute.start.get**"
                        + "\n* **guild.tribute.timeout.get**"
                )
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptRole(event, role));
    }

    private void acceptRole(CommandEvent event, Role role) {
        event.getPermissionService().setRolePermission(role, Node.GEOBOT_MOD, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_ACTIVITY_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_INACTIVITY_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_SET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_START_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_TIMEOUT_GET, Boolean.TRUE);
        event.reply(CommandReaction.OK, event::complete);
    }

}
