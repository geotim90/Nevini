package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class PermissionAllowCommand extends Command {

    public PermissionAllowCommand() {
        super(CommandDescriptor.builder()
                .keyword("allow")
                .aliases(new String[]{"add", "grant"})
                .module(Module.CORE)
                .node(Node.CORE_PERMISSION_ALLOW)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_PERMISSIONS})
                .description("configures permission overrides to allow access to commands")
                .syntax("<options>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
