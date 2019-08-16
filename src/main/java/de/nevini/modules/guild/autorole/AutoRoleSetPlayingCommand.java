package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Role;

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
                .details("Restrictions on who can be assigned playing roles may be applied on node **"
                        + Node.GUILD_AUTO_ROLE_PLAYING.getNode() + "** (only server level overrides are applicable).")
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
