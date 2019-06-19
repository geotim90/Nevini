package de.nevini.bot.modules.guild.autorole;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.scope.Permissions;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
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
