package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.legacy.role.RemoveRoleCommand;
import org.springframework.stereotype.Component;

@Component
public class RemoveCommand extends Command {

    public RemoveCommand() {
        super(CommandDescriptor.builder()
                .keyword("remove")
                .children(new Command[]{
                        new RemoveRoleCommand()
                })
                .description("executes legacy `remove` commands")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // delegate to child despite missing "role" keyword
        getChildren()[0].onEvent(event);
    }

}
