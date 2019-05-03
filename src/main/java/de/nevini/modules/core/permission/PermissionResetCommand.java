package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class PermissionResetCommand extends Command {

    public PermissionResetCommand() {
        super(CommandDescriptor.builder()
                .keyword("reset")
                .aliases(new String[]{"clear", "default"})
                .module(Module.CORE)
                .node(Node.CORE_PERMISSION_RESET)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_PERMISSIONS})
                .description("resets permission overrides for commands")
                .syntax("<options>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
