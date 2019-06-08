package de.nevini.bot.modules.core.prefix;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class PrefixCommand extends Command {

    public PrefixCommand() {
        super(CommandDescriptor.builder()
                .keyword("prefix")
                .children(new Command[]{
                        new PrefixGetCommand(),
                        new PrefixSetCommand()
                })
                .description("displays and configures the command prefix")
                .details("By default, this command will behave the same as **prefix get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
