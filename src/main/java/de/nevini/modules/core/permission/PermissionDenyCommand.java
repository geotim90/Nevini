package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class PermissionDenyCommand extends Command {

    public PermissionDenyCommand() {
        super(CommandDescriptor.builder()
                .keyword("deny")
                .aliases(new String[]{"block", "refuse"})
                .module(Module.CORE)
                .node(Node.CORE_PERMISSION_DENY)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_PERMISSIONS})
                .description("configures permission overrides for commands")
                .syntax("<options>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
