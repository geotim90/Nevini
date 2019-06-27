package de.nevini.bot.modules.guild.activity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.modules.guild.inactivity.ReportCommand;
import org.springframework.stereotype.Component;

@Component
public class ActivityCommand extends Command {

    public ActivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("activity")
                .children(new Command[]{
                        new ActivityGetCommand(),
                        new ActivitySetCommand(),
                        new ReportCommand()
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
