package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

public class GetGameCommand extends Command {

    public GetGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .children(new Command[]{
                        new GetGameTimeoutCommand()
                })
                .description("Geobot style `get game` command emulation")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
