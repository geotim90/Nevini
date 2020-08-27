package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleUnsetJoinCommand extends Command {

    AutoRoleUnsetJoinCommand() {
        super(CommandDescriptor.builder()
                .keyword("join")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("stops auto-roles for users that join the server")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getAutoRoleService().removeJoinAutoRole(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
