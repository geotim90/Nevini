package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class UnsetGameCommand extends Command {

    public UnsetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .children(new Command[]{
                        new UnsetGameTimeoutCommand()
                })
                .description("removes the lastPlayed timeout for a game")
                .syntax("timeout <game>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // delegate to child despite missing "timeout" keyword
        getChildren()[0].onEvent(event);
    }

}
