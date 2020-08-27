package de.nevini.modules.geobot.role;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import net.dv8tion.jda.api.entities.Role;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class GetRoleMemberCommand extends Command {

    GetRoleMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .node(Node.GEOBOT_ADMIN)
                .description("displays the roles for users to include in inactivity reports")
                .details("This checks the server level role permission overrides for node **geobot.user**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        List<String> roles = event.getPermissionService()
                .getRolePermissions(event.getGuild(), Node.GEOBOT_USER)
                .stream().filter(e -> e.getFlag() > 0).map(e -> event.getGuild().getRoleById(e.getId()))
                .filter(Objects::nonNull).map(Role::getName).collect(Collectors.toList());
        if (roles.isEmpty()) {
            event.reply("No member role has been configured.", event::complete);
        } else {
            event.reply("The configured member roles are **" + StringUtils.join(roles, "**, **") + "**.",
                    event::complete);
        }
    }

}
