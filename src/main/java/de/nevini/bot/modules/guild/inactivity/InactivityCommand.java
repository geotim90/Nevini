package de.nevini.bot.modules.guild.inactivity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class InactivityCommand extends Command {

    public InactivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("inactivity")
                .children(new Command[]{
                        new InactivityGetCommand(),
                        new InactivitySetCommand(),
                        new InactivityUnsetCommand(),
                        new InactivityReportCommand()
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
