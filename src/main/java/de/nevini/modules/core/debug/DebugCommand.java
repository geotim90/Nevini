package de.nevini.modules.core.debug;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import org.springframework.stereotype.Component;

@Component
public class DebugCommand extends Command {

    public DebugCommand() {
        super(CommandDescriptor.builder()
                .keyword("debug")
                .children(new Command[]{
                                new DebugPermissionCommand()
                        }
                )
                .ownerOnly(true)
                .node(Node.CORE_HELP) // dummy node
                .description("various commands for debugging and analysis")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // do nothing
    }

}
