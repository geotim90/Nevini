package de.nevini.modules.core.prefix;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class PrefixCommand extends Command {

    protected PrefixCommand() {
        super(CommandDescriptor.builder()
                .keyword("prefix")
                .children(new Command[]{
                        new PrefixGetCommand(),
                        new PrefixSetCommand()
                })
                .description("configures the command prefix")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}