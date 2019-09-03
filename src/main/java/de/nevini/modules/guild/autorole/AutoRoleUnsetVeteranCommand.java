package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleUnsetVeteranCommand extends Command {

    AutoRoleUnsetVeteranCommand() {
        super(CommandDescriptor.builder()
                .keyword("veteran")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("stops auto-roles for users that joined the server some time ago")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.DURATION.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.DURATION.resolveArgumentOrOptionOrInput(event, duration -> acceptDuration(event, duration));
    }

    private void acceptDuration(CommandEvent event, Long duration) {
        event.getAutoRoleService().removeVeteranAutoRole(duration, event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
