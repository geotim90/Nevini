package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class PermissionDebugCommand extends Command {

    public PermissionDebugCommand() {
        super(CommandDescriptor.builder()
                .keyword("debug")
                .node(Node.CORE_PERMISSION_DEBUG)
                .description("displays permissions overrides for commands")
                .syntax("--node <node> [<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, target -> acceptTarget(event, target)).get();
    }

    private void acceptTarget(CommandEvent event, PermissionOptions permissionTarget) {
        if (permissionTarget.isAll() || permissionTarget.getNodes().size() > 1) {
            event.reply(CommandReaction.WARNING, "Too many nodes matched your input! Please be more specific next time.");
        } else if (permissionTarget.getNodes().isEmpty()) {
            event.reply(CommandReaction.WARNING, "You did not provide a node!");
        } else {
            // TODO display result
        }
    }

}
