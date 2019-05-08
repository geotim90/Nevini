package de.nevini.modules.legacy;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.legacy.role.AddRoleCommand;
import org.springframework.stereotype.Component;

@Component
public class AddCommand extends Command {

    public AddCommand() {
        super(CommandDescriptor.builder()
                .keyword("add")
                .children(new Command[]{
                        new AddRoleCommand()
                })
                .description("executes legacy `add` commands")
                .syntax("role (initiate|member|mod|admin) <role>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
