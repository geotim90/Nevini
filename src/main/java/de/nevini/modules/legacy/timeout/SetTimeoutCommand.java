package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class SetTimeoutCommand extends Command {

    public SetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.LEGACY_SET_TIMEOUT)
                .description("configures timeouts in days")
                .syntax("(joined|lastOnline|lastMessage) <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
