package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

public class SetGameCommand extends Command {

    public SetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .children(new Command[]{
                        new SetGameTimeoutCommand()
                })
                .description("configures the lastPlayed timeout for a game in days")
                .syntax("timeout <game> <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // delegate to child despite missing "timeout" keyword
        getChildren()[0].onEvent(event);
    }

}
