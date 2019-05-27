package de.nevini.modules.core.permission;

import de.nevini.command.CommandDescriptor;
import de.nevini.scope.Node;

public class PermissionAllowCommand extends PermissionSetCommand {

    public PermissionAllowCommand() {
        super(CommandDescriptor.builder()
                .keyword("allow")
                .aliases(new String[]{"add", "grant"})
                .node(Node.CORE_PERMISSION_ALLOW)
                .description("configures permission node overrides for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors())
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build(), "allow", true);
    }

}
