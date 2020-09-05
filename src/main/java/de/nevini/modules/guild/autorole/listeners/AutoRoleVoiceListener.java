package de.nevini.modules.guild.autorole.listeners;

import de.nevini.core.scope.Node;
import de.nevini.modules.core.permission.services.PermissionService;
import de.nevini.modules.guild.autorole.services.AutoRoleService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRoleVoiceListener {

    private final AutoRoleService autoRoleService;
    private final PermissionService permissionService;

    public AutoRoleVoiceListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired PermissionService permissionService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        this.permissionService = permissionService;
        eventDispatcher.subscribe(GuildVoiceJoinEvent.class, e -> onVoiceJoin(e.getMember(), e.getChannelJoined()));
        eventDispatcher.subscribe(GuildVoiceLeaveEvent.class, e -> onVoiceLeave(e.getMember(), e.getChannelLeft()));
        eventDispatcher.subscribe(GuildVoiceMoveEvent.class, e -> {
            onVoiceLeave(e.getMember(), e.getChannelLeft());
            onVoiceJoin(e.getMember(), e.getChannelJoined());
        });
    }

    private void onVoiceJoin(Member member, VoiceChannel channel) {
        // ignore bots
        if (member.getUser().isBot()) return;
        // get role
        Role role = autoRoleService.getVoiceRole(channel);
        if (role == null) return;
        // add role if not present
        if (!member.getRoles().contains(role)) {
            // check permissions
            if (!member.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;
            if (!permissionService.hasUserPermission(member, Node.GUILD_AUTO_ROLE_VOICE)) return;
            // add role
            log.debug("Adding role {} to {}", role, member);
            member.getGuild().addRoleToMember(member, role).queue();
        }
    }

    private void onVoiceLeave(Member member, VoiceChannel channel) {
        // ignore bots
        if (member.getUser().isBot()) return;
        // get role
        Role role = autoRoleService.getVoiceRole(channel);
        if (role == null) return;
        // remove role if present
        if (member.getRoles().contains(role)) {
            // check permissions
            if (!member.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;
            if (!permissionService.hasUserPermission(member, Node.GUILD_AUTO_ROLE_VOICE)) return;
            // add role
            log.debug("Removing role {} from {}", role, member);
            member.getGuild().removeRoleFromMember(member, role).queue();
        }
    }

}
