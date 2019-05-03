package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class PermissionDebugCommand extends Command {

    public PermissionDebugCommand() {
        super(CommandDescriptor.builder()
                .keyword("debug")
                .module(Module.CORE)
                .node(Node.CORE_PERMISSION_DEBUG)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_PERMISSIONS})
                .description("displays permissions overrides for commands")
                .syntax("[<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
