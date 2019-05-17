package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.db.game.GameData;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;

public class UnsetGameTimeoutCommand extends Command {

    public UnsetGameTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.LEGACY_UNSET_GAME_TIMEOUT)
                .description("removes the lastPlayed timeout for a game")
                .syntax("<game>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveArgumentOrOptionOrInput(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        event.getLegacyTimeoutService().removeLastPlayedTimeout(event.getGuild(), game);
    }

}
