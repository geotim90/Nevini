package de.nevini.modules.legacy.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class RemoveRoleCommand extends Command {

    public RemoveRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .module(Module.LEGACY)
                .node(Node.LEGACY_ADD_ROLE)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("removes legacy permission level roles")
                .syntax("(initiate|member|mod|admin) <role>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
