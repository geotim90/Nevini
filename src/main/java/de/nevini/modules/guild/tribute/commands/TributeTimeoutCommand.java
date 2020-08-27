package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

class TributeTimeoutCommand extends Command {

    TributeTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .aliases(new String[]{"deadline"})
                .children(new Command[]{
                        new TributeTimeoutGetCommand(),
                        new TributeTimeoutSetCommand(),
                        new TributeTimeoutUnsetCommand(),
                        new TributeTimeoutFeedCommand()
                })
                .description("displays and configures the timeout for users that need to contribute")
                .details("By default, this command will behave the same as **tribute timeout get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }
}
