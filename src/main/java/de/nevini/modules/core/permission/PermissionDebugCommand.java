package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class PermissionDebugCommand extends Command {

    public PermissionDebugCommand() {
        super(CommandDescriptor.builder()
                .keyword("debug")
                .node(Node.CORE_PERMISSION_DEBUG)
                .description("displays permissions overrides for bot commands")
                .syntax("[--node] <node> [<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, true, false, options -> acceptOptions(event, options)).get();
    }

    private void acceptOptions(CommandEvent event, PermissionOptions options) {
        // TODO display result
    }

}
