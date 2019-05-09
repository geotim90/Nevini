package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class PermissionDenyCommand extends Command {

    public PermissionDenyCommand() {
        super(CommandDescriptor.builder()
                .keyword("deny")
                .aliases(new String[]{"block", "refuse"})
                .node(Node.CORE_PERMISSION_DENY)
                .description("configures permission overrides for bot commands")
                .syntax("( [--node] <node> | --all ) [<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, true, true, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        // TODO display result
    }

}
