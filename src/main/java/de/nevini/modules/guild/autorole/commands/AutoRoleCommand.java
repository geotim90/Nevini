package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class AutoRoleCommand extends Command {

    public AutoRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("auto-role")
                .aliases(new String[]{"auto-roles", "autorole", "autoroles", "ar"})
                .children(new Command[]{
                        new AutoRoleGetCommand(),
                        new AutoRoleSetCommand(),
                        new AutoRoleUnsetCommand()
                })
                .description("displays and configures auto-roles")
                .details("By default, this command will behave the same as **auto-role get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
