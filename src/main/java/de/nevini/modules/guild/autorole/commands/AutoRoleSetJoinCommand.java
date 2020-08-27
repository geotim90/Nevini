package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;

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
                .details("Restrictions on who can be assigned join roles may be applied on node **"
                        + Node.GUILD_AUTO_ROLE_JOIN.getNode() + "** (only server level overrides are applicable).")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptRole(event, role));
    }

    private void acceptRole(CommandEvent event, Role role) {
        event.getAutoRoleService().setJoinAutoRole(role);
        event.reply(CommandReaction.OK, event::complete);
    }

}
