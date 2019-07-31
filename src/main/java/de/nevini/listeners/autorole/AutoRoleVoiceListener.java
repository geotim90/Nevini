package de.nevini.listeners.autorole;

import de.nevini.services.common.AutoRoleService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
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
        eventDispatcher.subscribe(GuildVoiceJoinEvent.class, this::onVoiceJoin);
        eventDispatcher.subscribe(GuildVoiceLeaveEvent.class, this::onVoiceLeave);
    }

    private void onVoiceJoin(GuildVoiceJoinEvent e) {
        // get role
        Role role = autoRoleService.getVoiceRole(e.getChannelJoined());
        if (role == null) return;

        // check permissions
        if (!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;

        // add role if not present
        if (!e.getMember().getRoles().contains(role)) {
            log.debug("Adding role {} to {}", role, e.getMember());
            e.getGuild().getController().addSingleRoleToMember(e.getMember(), role).queue();
        }
    }

    private void onVoiceLeave(GuildVoiceLeaveEvent e) {
        // get role
        Role role = autoRoleService.getVoiceRole(e.getChannelLeft());
        if (role == null) return;

        // check permissions
        if (!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;

        // remove role if present
        if (e.getMember().getRoles().contains(role)) {
            log.debug("Removing role {} from {}", role, e.getMember());
            e.getGuild().getController().removeSingleRoleFromMember(e.getMember(), role).queue();
        }
    }

}
