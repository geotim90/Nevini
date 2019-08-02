package de.nevini.listeners.tribute;

import de.nevini.services.common.TributeService;
import de.nevini.util.concurrent.EventDispatcher;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TributeRoleListener {

    private final TributeService tributeService;

    public TributeRoleListener(
            @Autowired TributeService tributeService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.tributeService = tributeService;
        eventDispatcher.subscribe(GuildMemberRoleAddEvent.class, this::onMemberRoleAdd);
    }

    private void onMemberRoleAdd(GuildMemberRoleAddEvent e) {
        Role role = tributeService.getRole(e.getGuild());
        if (role != null && e.getRoles().contains(role)) {
            tributeService.addMember(e.getMember());
        }
    }

}
