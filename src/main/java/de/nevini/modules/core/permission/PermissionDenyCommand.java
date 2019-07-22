package de.nevini.modules.core.permission;

import de.nevini.command.CommandDescriptor;
import de.nevini.scope.Node;

class PermissionDenyCommand extends PermissionSetCommand {

    PermissionDenyCommand() {
        super(CommandDescriptor.builder()
                .keyword("deny")
                .aliases(new String[]{"block", "refuse"})
                .node(Node.CORE_PERMISSION_DENY)
                .description("configures permission node overrides for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors(true))
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build(), "deny", false);
    }

}
