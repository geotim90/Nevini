package de.nevini.modules.guild.inactivity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class InactivityCommand extends Command {

    public InactivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("inactivity")
                .children(new Command[]{
                        new InactivityGetCommand(),
                        new InactivitySetCommand(),
                        new InactivityUnsetCommand()
                })
                .description("displays and configures user inactivity information")
                .details("By default, this command will behave the same as **inactivity get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
