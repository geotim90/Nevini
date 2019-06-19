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
import net.dv8tion.jda.core.entities.Role;

@Slf4j
class AutoRoleSetPlayingCommand extends Command {

    AutoRoleSetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("configures auto-roles for users that are currently playing a certain game")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(),
                        Resolvers.ROLE.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        Resolvers.ROLE.resolveArgumentOrOptionOrInput(event, role -> acceptGameAndRole(event, game, role));
    }

    private void acceptGameAndRole(CommandEvent event, GameData game, Role role) {
        event.getAutoRoleService().setPlayingAutoRole(game, role);
        event.reply(CommandReaction.OK, event::complete);
    }

}
