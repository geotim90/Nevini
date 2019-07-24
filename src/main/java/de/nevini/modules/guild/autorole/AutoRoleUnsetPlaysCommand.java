package de.nevini.modules.guild.autorole;

import de.nevini.command.*;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleUnsetPlaysCommand extends Command {

    AutoRoleUnsetPlaysCommand() {
        super(CommandDescriptor.builder()
                .keyword("plays")
                .node(Node.GUILD_AUTO_ROLE_UNSET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("stops auto-roles for users that ever play a certain game")
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
        event.getAutoRoleService().removePlaysAutoRole(game, event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}