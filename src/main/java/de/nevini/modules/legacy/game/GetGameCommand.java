package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class GetGameCommand extends Command {

    public GetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .children(new Command[]{
                        new GetGameTimeoutCommand()
                })
                .description("displays the lastPlayed timeout for a game")
                .syntax("timeout <game>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // delegate to child despite missing "timeout" keyword
        getChildren()[0].onEvent(event);
    }

}
