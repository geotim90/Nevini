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
                .details("Providing a role will cause the bot to automatically assign said role "
                        + "when a user plays a certain game.\n"
                        + "If no role is provided, the bot will stop automatically assigning roles "
                        + "under the aforementioned conditions.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        Resolvers.ROLE.resolveOptionOrDefault(event, null, role -> acceptGameAndRole(event, game, role));
    }

    private void acceptGameAndRole(CommandEvent event, GameData game, Role role) {
        if (role == null) {
            event.getAutoRoleService().removePlaysAutoRole(game, event.getGuild());
        } else {
            event.getAutoRoleService().setPlaysAutoRole(game, role);
        }
        event.reply(CommandReaction.OK, event::complete);
    }

}
