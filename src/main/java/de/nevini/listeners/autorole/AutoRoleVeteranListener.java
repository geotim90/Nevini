package de.nevini.listeners.autorole;

import de.nevini.jpa.autorole.AutoRoleData;
import de.nevini.scope.Node;
import de.nevini.services.common.AutoRoleService;
import de.nevini.services.common.PermissionService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Iterator;

@Slf4j
@Component
public class AutoRoleVeteranListener {

    private final AutoRoleService autoRoleService;
    private final PermissionService permissionService;

    public AutoRoleVeteranListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired PermissionService permissionService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        this.permissionService = permissionService;
        eventDispatcher.subscribe(UserUpdateOnlineStatusEvent.class, this::onUpdateOnlineStatus);
    }

    private void onUpdateOnlineStatus(UserUpdateOnlineStatusEvent e) {
        // ignore bots
        if (e.getUser().isBot()) return;
        // iterate over mutual guilds
        for (Guild guild : e.getUser().getMutualGuilds()) {
            // check permission
            if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) continue;
            // get member
            Member member = guild.getMember(e.getUser());
            if (member == null) continue;
            // check permission
            if (!permissionService.hasUserPermission(e.getMember(), Node.GUILD_AUTO_ROLE_VETERAN)) continue;
            // iterate over configured veteran auto roles for this guild
            Iterator<AutoRoleData> iterator = autoRoleService.getVeteranAutoRoles(guild).iterator();
            while (iterator.hasNext()) {
                AutoRoleData autoRole = iterator.next();
                // check role still exists
                Role role = guild.getRoleById(autoRole.getRole());
                if (role == null) continue;
                // check member eligibility
                if (member.getTimeJoined().isAfter(OffsetDateTime.now().minusDays(autoRole.getId()))) continue;
                // add role if not present
                if (!member.getRoles().contains(role)) {
                    log.debug("Adding role {} to {} in {}", role, member, guild);
                    guild.addRoleToMember(member, role).queue();
                }
                break;
            }
            // remove lower tiered roles
            while (iterator.hasNext()) {
                AutoRoleData autoRole = iterator.next();
                // check role still exists
                Role role = guild.getRoleById(autoRole.getRole());
                if (role == null) continue;
                // remove role if present
                if (member.getRoles().contains(role)) {
                    log.debug("Removing role {} from {} in {}", role, member, guild);
                    guild.removeRoleFromMember(member, role).queue();
                }
            }
        }
    }

}
