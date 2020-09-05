package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleUnsetPlayingCommand extends Command {

    AutoRoleUnsetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("stops auto-roles for users that are currently playing a certain game")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveArgumentOrOptionOrInput(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        event.getAutoRoleService().removePlayingAutoRole(game, event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
