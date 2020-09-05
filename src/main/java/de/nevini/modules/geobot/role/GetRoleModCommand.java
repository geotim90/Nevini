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

class GetRoleModCommand extends Command {

    GetRoleModCommand() {
        super(CommandDescriptor.builder()
                .keyword("mod")
                .aliases(new String[]{"moderator"})
                .node(Node.GEOBOT_ADMIN)
                .description("displays server level role permission overrides for node **geobot.mod**")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        List<String> roles = event.getPermissionService()
                .getRolePermissions(event.getGuild(), Node.GEOBOT_MOD)
                .stream().filter(e -> e.getFlag() > 0).map(e -> event.getGuild().getRoleById(e.getId()))
                .filter(Objects::nonNull).map(Role::getName).collect(Collectors.toList());
        if (roles.isEmpty()) {
            event.reply("No mod role has been configured.", event::complete);
        } else {
            event.reply("The configured mod roles are **" + StringUtils.join(roles, "**, **") + "**.",
                    event::complete);
        }
    }

}
