package de.nevini.modules.guild.autorole;

import de.nevini.command.*;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Role;

@Slf4j
class AutoRoleSetPlaysCommand extends Command {

    AutoRoleSetPlaysCommand() {
        super(CommandDescriptor.builder()
                .keyword("plays")
                .node(Node.GUILD_AUTO_ROLE_SET)
                .minimumBotPermissions(Permissions.sum(Permissions.BOT_EMBED, Permissions.MANAGE_ROLES))
                .description("configures auto-roles for users that ever play a certain game")
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
        event.getAutoRoleService().setPlaysAutoRole(game, role);
        event.reply(CommandReaction.OK, event::complete);
    }

}