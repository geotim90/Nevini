package de.nevini.modules.core.permission.commands;

import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.scope.Node;

class PermissionResetCommand extends PermissionSetCommand {

    PermissionResetCommand() {
        super(CommandDescriptor.builder()
                .keyword("reset")
                .aliases(new String[]{"clear", "default"})
                .node(Node.CORE_PERMISSION_RESET)
                .description("resets permission node overrides for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors(true))
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build(), "reset", null);
    }

}
