package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class PermissionResetCommand extends Command {

    public PermissionResetCommand() {
        super(CommandDescriptor.builder()
                .keyword("reset")
                .aliases(new String[]{"clear", "default"})
                .node(Node.CORE_PERMISSION_RESET)
                .description("resets permission overrides for bot commands")
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
