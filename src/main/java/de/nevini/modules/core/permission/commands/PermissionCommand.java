package de.nevini.modules.core.permission.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class PermissionCommand extends Command {

    public PermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .aliases(new String[]{"permissions", "perm", "perms"})
                .children(new Command[]{
                        new PermissionGetCommand(),
                        new PermissionAllowCommand(),
                        new PermissionDenyCommand(),
                        new PermissionResetCommand(),
                        new PermissionDebugCommand(),
                        new PermissionOverridesCommand(),
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
