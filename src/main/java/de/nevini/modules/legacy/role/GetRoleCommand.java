package de.nevini.modules.legacy.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class GetRoleCommand extends Command {

    public GetRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .module(Module.LEGACY)
                .node(Node.LEGACY_GET_ROLE)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("displays legacy permission level roles")
                .syntax("(initiate|member|mod|admin)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
