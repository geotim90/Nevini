package de.nevini.modules.legacy.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class RemoveRoleCommand extends Command {

    public RemoveRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .node(Node.LEGACY_ADD_ROLE)
                .description("removes legacy permission level roles")
                .syntax("(initiate|member|mod|admin) <role>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
