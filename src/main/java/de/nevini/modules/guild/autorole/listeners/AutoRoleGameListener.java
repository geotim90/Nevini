package de.nevini.modules.guild.autorole.listeners;

import de.nevini.core.scope.Node;
import de.nevini.modules.core.permission.services.PermissionService;
import de.nevini.modules.guild.autorole.jpa.AutoRoleData;
import de.nevini.modules.guild.autorole.services.AutoRoleService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRoleGameListener {

    private final AutoRoleService autoRoleService;
    private final PermissionService permissionService;

    public AutoRoleGameListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired PermissionService permissionService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        this.permissionService = permissionService;
        eventDispatcher.subscribe(UserActivityStartEvent.class, this::onUserActivityStart);
        eventDispatcher.subscribe(UserActivityEndEvent.class, this::onUserActivityEnd);
    }

    private void onUserActivityStart(UserActivityStartEvent e) {
        if (!e.getUser().isBot() && e.getNewActivity().getType() == Activity.ActivityType.DEFAULT) {
            RichPresence presence = e.getNewActivity().asRichPresence();
            if (presence != null) {
                // iterate over matching auto-roles
                for (AutoRoleData autoRole : autoRoleService.getGameAutoRoles(presence.getApplicationIdLong())) {
                    // look for guild, member and role
                    Guild guild = e.getJDA().getGuildById(autoRole.getGuild());
                    if (guild == null) continue;
                    Member member = guild.getMember(e.getUser());
                    if (member == null) continue;
                    Role role = guild.getRoleById(autoRole.getRole());
                    if (role == null) continue;
                    // add role if not present
                    if (!member.getRoles().contains(role)) {
                        // check permission
                        if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) continue;
                        if (!permissionService.hasUserPermission(member, Node.GUILD_AUTO_ROLE_PLAYING)) continue;
                        // add role
                        log.debug("Adding role {} to {}", role, member);
                        guild.addRoleToMember(member, role).queue();
                    }
                }
            }
        }
    }

    private void onUserActivityEnd(UserActivityEndEvent e) {
        if (!e.getUser().isBot() && e.getOldActivity().getType() == Activity.ActivityType.DEFAULT) {
            RichPresence presence = e.getOldActivity().asRichPresence();
            if (presence != null) {
                // iterate over matching auto-roles
                for (AutoRoleData autoRole : autoRoleService.getPlayingAutoRoles(presence.getApplicationIdLong())) {
                    // look for guild, member and role
                    Guild guild = e.getJDA().getGuildById(autoRole.getGuild());
                    if (guild == null) continue;
                    Member member = guild.getMember(e.getUser());
                    if (member == null) continue;
                    Role role = guild.getRoleById(autoRole.getRole());
                    if (role == null) continue;
                    // remove role if present
                    if (member.getRoles().contains(role)) {
                        // check permission
                        if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) continue;
                        // add role
                        log.debug("Removing role {} from {}", role, member);
                        guild.removeRoleFromMember(member, role).queue();
                    }
                }
            }
        }
    }

}
