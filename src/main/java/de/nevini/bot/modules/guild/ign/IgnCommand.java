package de.nevini.bot.modules.guild.ign;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class IgnCommand extends Command {

    public IgnCommand() {
        super(CommandDescriptor.builder()
                .keyword("ign")
                .children(new Command[]{
                        new IgnGetCommand(),
                        new IgnSetCommand()
                })
                .description("displays and configures in-game names")
                .details("By default, this command will behave the same as **ign get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
