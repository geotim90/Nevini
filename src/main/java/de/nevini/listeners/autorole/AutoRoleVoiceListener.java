package de.nevini.listeners.autorole;

import de.nevini.services.common.AutoRoleService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRoleVoiceListener {

    private final AutoRoleService autoRoleService;

    public AutoRoleVoiceListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        eventDispatcher.subscribe(GuildVoiceJoinEvent.class, e -> onVoiceJoin(e.getMember(), e.getChannelJoined()));
        eventDispatcher.subscribe(GuildVoiceLeaveEvent.class, e -> onVoiceLeave(e.getMember(), e.getChannelLeft()));
        eventDispatcher.subscribe(GuildVoiceMoveEvent.class, e -> {
            onVoiceLeave(e.getMember(), e.getChannelLeft());
            onVoiceJoin(e.getMember(), e.getChannelJoined());
        });
    }

    private void onVoiceJoin(Member member, VoiceChannel channel) {
        // get role
        Role role = autoRoleService.getVoiceRole(channel);
        if (role == null) return;

        // check permissions
        if (!member.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;

        // add role if not present
        if (!member.getRoles().contains(role)) {
            log.debug("Adding role {} to {}", role, member);
            member.getGuild().getController().addSingleRoleToMember(member, role).queue();
        }
    }

    private void onVoiceLeave(Member member, VoiceChannel channel) {
        // get role
        Role role = autoRoleService.getVoiceRole(channel);
        if (role == null) return;

        // check permissions
        if (!member.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;

        // remove role if present
        if (member.getRoles().contains(role)) {
            log.debug("Removing role {} from {}", role, member);
            member.getGuild().getController().removeSingleRoleFromMember(member, role).queue();
        }
    }

    private void onVoiceMove(GuildVoiceMoveEvent e) {

    }

}