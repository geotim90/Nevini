package de.nevini.modules.legacy.role;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class GetRoleCommand extends Command {

    public GetRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .node(Node.LEGACY_GET_ROLE)
                .description("displays legacy permission level roles")
                .syntax("(initiate|member|mod|admin)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
