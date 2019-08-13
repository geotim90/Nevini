package de.nevini.modules.geobot.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Role;

class RemoveRoleAdminCommand extends Command {

    RemoveRoleAdminCommand() {
        super(CommandDescriptor.builder()
                .keyword("admin")
                .aliases(new String[]{"administrator"})
                .node(Node.GEOBOT_ADMIN)
                .description("configures server level role permission overrides for node **geobot.admin**")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.ROLE.describe(false, true)
                })
                .details("This command also configures server level role permission override for the following nodes:"
                        + "\n* **guild.activity.get**"
                        + "\n* **guild.activity.set**"
                        + "\n* **guild.inactivity.get**"
                        + "\n* **guild.inactivity.set**"
                        + "\n* **guild.tribute.get**"
                        + "\n* **guild.tribute.set**"
                        + "\n* **guild.tribute.role.get**"
                        + "\n* **guild.tribute.role.set**"
                        + "\n* **guild.tribute.start.get**"
                        + "\n* **guild.tribute.start.set**"
                        + "\n* **guild.tribute.timeout.get**"
                        + "\n* **guild.tribute.timeout.set**"
                )
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptRole(event, role));
    }

    private void acceptRole(CommandEvent event, Role role) {
        event.getPermissionService().setRolePermission(role, Node.GEOBOT_ADMIN, null);
        event.getPermissionService().setRolePermission(role, Node.GUILD_ACTIVITY_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_INACTIVITY_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_SET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_START_GET, Boolean.TRUE);
        event.getPermissionService().setRolePermission(role, Node.GUILD_TRIBUTE_TIMEOUT_GET, Boolean.TRUE);
        event.reply(CommandReaction.OK, event::complete);
    }

}
