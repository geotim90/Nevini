package de.nevini.bot.modules.guild.autorole;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Role;

@Slf4j
class AutoRoleSetJoinCommand extends Command {

    AutoRoleSetJoinCommand() {
        super(CommandDescriptor.builder()
                .keyword("join")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("configures auto-roles for users that join the server")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.ROLE.describe(false, true)
                })
                .details("Providing a role will cause the bot to automatically assign said role "
                        + "when a user joins the server.\n"
                        + "If no role is provided, the bot will stop automatically assigning roles "
                        + "under the aforementioned conditions.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveOptionOrDefault(event, null, role -> acceptRole(event, role));
    }

    private void acceptRole(CommandEvent event, Role role) {
        if (role == null) {
            event.getAutoRoleService().removeJoinAutoRole(event.getGuild());
        } else {
            event.getAutoRoleService().setJoinAutoRole(role);
        }
        event.reply(CommandReaction.OK, event::complete);
    }

}
