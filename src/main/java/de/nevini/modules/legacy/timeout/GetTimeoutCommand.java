package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class GetTimeoutCommand extends Command {

    public GetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.LEGACY_GET_TIMEOUT)
                .description("displays timeouts in days")
                .syntax("(joined|lastOnline|lastMessage)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
