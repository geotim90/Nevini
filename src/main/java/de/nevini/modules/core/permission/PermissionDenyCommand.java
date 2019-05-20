package de.nevini.modules.core.permission;

import de.nevini.command.CommandDescriptor;
import de.nevini.modules.Node;

public class PermissionDenyCommand extends PermissionSetCommand {

    public PermissionDenyCommand() {
        super(CommandDescriptor.builder()
                .keyword("deny")
                .aliases(new String[]{"block", "refuse"})
                .node(Node.CORE_PERMISSION_DENY)
                .description("configures permission node overrides for bot commands")
                .syntax("( [--node] <node> | --all ) [<options>]")
                .build(), "deny", false);
    }

}
