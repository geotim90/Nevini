package de.nevini.listeners.autorole;

import de.nevini.services.common.AutoRoleService;
import de.nevini.util.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRoleJoinListener {

    private final AutoRoleService autoRoleService;

    public AutoRoleJoinListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        eventDispatcher.subscribe(GuildMemberJoinEvent.class, this::onMemberJoin);
    }

    private void onMemberJoin(GuildMemberJoinEvent e) {
        // get role
        Role joinRole = autoRoleService.getJoinRole(e.getGuild());
        if (joinRole == null) return;

        // check permissions
        if (!e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) return;

        // add role if not present
        if (!e.getMember().getRoles().contains(joinRole)) {
            log.debug("Adding role {} to {}", joinRole, e.getMember());
            e.getGuild().getController().addSingleRoleToMember(e.getMember(), joinRole).queue();
        }
    }

}
