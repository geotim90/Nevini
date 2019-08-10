package de.nevini.listeners.autorole;

import de.nevini.jpa.autorole.AutoRoleData;
import de.nevini.services.common.AutoRoleService;
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

@Slf4j
@Component
public class AutoRoleVeteranListener {

    private final AutoRoleService autoRoleService;

    public AutoRoleVeteranListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        eventDispatcher.subscribe(UserUpdateOnlineStatusEvent.class, this::onUpdateOnlineStatus);
    }

    private void onUpdateOnlineStatus(UserUpdateOnlineStatusEvent e) {
        // iterate over mutual guilds
        for (Guild guild : e.getUser().getMutualGuilds()) {
            // check permission
            if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) continue;
            // iterate over configured veteran auto roles for this guild
            for (AutoRoleData autoRole : autoRoleService.getVeteranAutoRoles(guild)) {
                // check role still exists
                Role role = guild.getRoleById(autoRole.getRole());
                if (role == null) continue;
                // check member eligibility
                Member member = guild.getMember(e.getUser());
                if (member == null) continue;
                if (member.getTimeJoined().isAfter(OffsetDateTime.now().minusDays(autoRole.getId()))) continue;
                // add role if not present
                if (!member.getRoles().contains(role)) {
                    log.debug("Adding role {} to {}", role, member);
                    guild.addRoleToMember(member, role).queue();
                }
            }
        }
    }

}
