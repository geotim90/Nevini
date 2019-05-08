package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class UnsetTimeoutCommand extends Command {

    public UnsetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.LEGACY_UNSET_TIMEOUT)
                .description("removes timeouts")
                .syntax("(joined|lastOnline|lastMessage)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
