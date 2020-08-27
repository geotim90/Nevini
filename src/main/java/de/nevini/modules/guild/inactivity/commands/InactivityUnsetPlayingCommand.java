package de.nevini.modules.guild.inactivity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;

public class InactivityUnsetPlayingCommand extends Command {

    public InactivityUnsetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .aliases(new String[]{"played", "last-played", "lastplayed"})
                .node(Node.GUILD_INACTIVITY_SET)
                .description("removes a user game inactivity threshold")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        event.getInactivityService().removePlayingThreshold(event.getGuild(), game);
        event.reply(CommandReaction.OK, event::complete);
    }

}
