package de.nevini.modules.guild.autorole.listeners;

import de.nevini.core.scope.Node;
import de.nevini.modules.core.permission.services.PermissionService;
import de.nevini.modules.guild.autorole.services.AutoRoleService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRoleJoinListener {

    private final AutoRoleService autoRoleService;
    private final PermissionService permissionService;

    public AutoRoleJoinListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired PermissionService permissionService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        this.permissionService = permissionService;
        eventDispatcher.subscribe(GuildMemberJoinEvent.class, this::onMemberJoin);
    }

    private void onMemberJoin(GuildMemberJoinEvent e) {
        // ignore bots
        if (e.getUser().isBot()) return;
        // get role
        Role joinRole = autoRoleService.getJoinRole(e.getGuild());
        if (joinRole == null) return;
        // add role if not present
        if (!e.getMember().getRoles().contains(joinRole)) {
            // check permissions
            if (!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;
            if (!permissionService.hasUserPermission(e.getMember(), Node.GUILD_AUTO_ROLE_JOIN)) return;
            // add role
            log.debug("Adding role {} to {}", joinRole, e.getMember());
            e.getGuild().addRoleToMember(e.getMember(), joinRole).queue();
        }
    }

}
