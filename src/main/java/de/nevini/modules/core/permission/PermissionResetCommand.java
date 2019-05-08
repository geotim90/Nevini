package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

import java.util.Arrays;

public class PermissionResetCommand extends Command {

    public PermissionResetCommand() {
        super(CommandDescriptor.builder()
                .keyword("reset")
                .aliases(new String[]{"clear", "default"})
                .node(Node.CORE_PERMISSION_RESET)
                .description("resets permission overrides for commands")
                .syntax("( --all | --node <node> ) [<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionOptions(event, target -> acceptTarget(event, target)).get();
    }

    private void acceptTarget(CommandEvent event, PermissionOptions permissionTarget) {
        if (permissionTarget.getNodes().isEmpty()) {
            if (permissionTarget.isAll()) {
                permissionTarget.setNodes(Arrays.asList(Node.values()));
            } else {
                event.reply(CommandReaction.WARNING, "You did not provide a node!");
                return;
            }
        }
        // TODO display result
    }

}
