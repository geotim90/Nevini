package de.nevini.modules.core.permission.commands;

import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.scope.Node;

class PermissionAllowCommand extends PermissionSetCommand {

    PermissionAllowCommand() {
        super(CommandDescriptor.builder()
                .keyword("allow")
                .aliases(new String[]{"add", "grant"})
                .node(Node.CORE_PERMISSION_ALLOW)
                .description("configures permission node overrides for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors(true))
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build(), "allow", true);
    }

}
