package de.nevini.modules.guild.ign.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class IgnCommand extends Command {

    public IgnCommand() {
        super(CommandDescriptor.builder()
                .keyword("ign")
                .children(new Command[]{
                        new IgnGetCommand(),
                        new IgnSetCommand(),
                        new IgnUnsetCommand(),
                        new IgnMissingCommand()
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
