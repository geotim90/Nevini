package de.nevini.modules.core.permission;

import de.nevini.command.CommandDescriptor;
import de.nevini.modules.Node;

public class PermissionResetCommand extends PermissionSetCommand {

    public PermissionResetCommand() {
        super(CommandDescriptor.builder()
                .keyword("reset")
                .aliases(new String[]{"clear", "default"})
                .node(Node.CORE_PERMISSION_RESET)
                .description("resets permission node overrides for bot commands")
                .options(PermissionOptions.getCommandOptionDescriptors())
                .details(PermissionOptions.getCommandDescriptorDetails())
                .build(), "reset", null);
    }

}
