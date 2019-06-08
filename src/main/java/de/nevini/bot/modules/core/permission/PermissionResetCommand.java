package de.nevini.bot.modules.core.permission;

import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.scope.Node;

public class PermissionResetCommand extends PermissionSetCommand {

    public PermissionResetCommand() {
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
