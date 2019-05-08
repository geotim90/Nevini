package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class SetGameCommand extends Command {

    public SetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .node(Node.LEGACY_SET_GAME)
                .description("configures the lastPlayed timeout for a game in days")
                .syntax("timeout <game> <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
