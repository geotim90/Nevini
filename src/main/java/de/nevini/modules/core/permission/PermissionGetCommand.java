package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PermissionGetCommand extends Command {

    public PermissionGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .module(Module.CORE)
                .node(Node.CORE_PERMISSION_GET)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_PERMISSIONS})
                .description("displays effective permissions for commands")
                .syntax("[<options>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        new PermissionTarget(event, target -> acceptTarget(event, target)).get();
    }

    private void acceptTarget(CommandEvent event, PermissionTarget permissionTarget) {
        if (permissionTarget.getNodes().isEmpty()) {
            if (permissionTarget.isAll()) {
                permissionTarget.setNodes(Arrays.asList(Node.values()));
            } else {
                permissionTarget.setNodes(Arrays.stream(Node.values())
                        .filter(node -> event.getModuleService().isModuleActive(event.getGuild(), node.getModule()))
                        .collect(Collectors.toList()));
            }
        }
        // TODO display result
    }

}
