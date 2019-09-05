package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class ActivityCommand extends Command {

    public ActivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("activity")
                .children(new Command[]{
                        new ActivityGetCommand(),
                        new ActivitySetCommand(),
                        new ActivityUnsetCommand(),
                        new ActivityReportCommand()
                })
                .description("displays and configures user activity information")
                .details("By default, this command will behave the same as **activity get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
