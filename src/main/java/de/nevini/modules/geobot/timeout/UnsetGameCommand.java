package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

public class UnsetGameCommand extends Command {

    public UnsetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .children(new Command[]{
                        new UnsetGameTimeoutCommand()
                })
                .description("Geobot style `get game` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
