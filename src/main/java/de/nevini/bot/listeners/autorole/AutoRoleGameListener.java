package de.nevini.bot.listeners.autorole;

import de.nevini.bot.db.autorole.AutoRoleData;
import de.nevini.bot.services.common.AutoRoleService;
import de.nevini.commons.concurrent.EventDispatcher;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AutoRoleGameListener {

    private final AutoRoleService autoRoleService;

    public AutoRoleGameListener(
            @Autowired AutoRoleService autoRoleService,
            @Autowired EventDispatcher eventDispatcher
    ) {
        this.autoRoleService = autoRoleService;
        eventDispatcher.subscribe(UserUpdateGameEvent.class, this::onUserUpdateGame);
    }

    private void onUserUpdateGame(UserUpdateGameEvent e) {
        if (!e.getUser().isBot()) {
            Game oldGame = e.getOldGame();
            Game newGame = e.getNewGame();
            if (newGame != null && newGame.isRich() && newGame.getType() == Game.GameType.DEFAULT) {
                onUserPlaying(e, newGame.asRichPresence());
            } else if (newGame == null && oldGame != null && oldGame.isRich() && oldGame.getType() == Game.GameType.DEFAULT) {
                onUserStoppedPlaying(e, oldGame.asRichPresence());
            }
        }
    }

    private void onUserPlaying(UserUpdateGameEvent e, RichPresence game) {
        // iterate over matching auto-roles
        for (AutoRoleData autoRole : autoRoleService.getGameAutoRoles(game.getApplicationIdLong())) {
            // look for guild, member and role
            Guild guild = e.getJDA().getGuildById(autoRole.getGuild());
            if (guild == null) continue;
            Member member = guild.getMember(e.getUser());
            if (member == null) continue;
            Role role = guild.getRoleById(autoRole.getRole());

            // check permission
            if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) continue;

            // add role if not present
            if (!member.getRoles().contains(role)) {
                log.debug("Adding role {} to {}", role, member);
                guild.getController().addSingleRoleToMember(member, role).queue();
            }
        }
    }

    private void onUserStoppedPlaying(UserUpdateGameEvent e, RichPresence game) {
        // iterate over matching auto-roles
        for (AutoRoleData autoRole : autoRoleService.getPlayingAutoRoles(game.getApplicationIdLong())) {
            // look for guild, member and role
            Guild guild = e.getJDA().getGuildById(autoRole.getGuild());
            if (guild == null) continue;
            Member member = guild.getMember(e.getUser());
            if (member == null) continue;
            Role role = guild.getRoleById(autoRole.getRole());

            // check permission
            if (!guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) continue;

            // remove role if present
            if (member.getRoles().contains(role)) {
                log.debug("Removing role {} from {}", role, member);
                guild.getController().removeSingleRoleFromMember(member, role).queue();
            }
        }
    }

}
