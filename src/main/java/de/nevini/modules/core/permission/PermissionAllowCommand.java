package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class PermissionAllowCommand extends Command {

    public PermissionAllowCommand() {
        super(CommandDescriptor.builder()
                .keyword("allow")
                .aliases(new String[]{"add", "grant"})
                .node(Node.CORE_PERMISSION_ALLOW)
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
