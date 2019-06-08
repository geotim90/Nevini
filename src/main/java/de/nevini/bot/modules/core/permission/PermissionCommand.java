package de.nevini.bot.modules.core.permission;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
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
