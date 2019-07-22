package de.nevini.modules.core.permission;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class PermissionCommand extends Command {

    public PermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .aliases(new String[]{"permissions", "perm", "perms"})
                .children(new Command[]{
                        new PermissionGetCommand(),
                        new PermissionDebugCommand(),
                        new PermissionAllowCommand(),
                        new PermissionDenyCommand(),
                        new PermissionResetCommand()
                })
                .description("displays and configures permission node overrides for bot commands")
                .details("By default, this command will behave the same as **permission get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
