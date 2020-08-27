package de.nevini.modules.guild.tribute.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

class TributeStartCommand extends Command {

    TributeStartCommand() {
        super(CommandDescriptor.builder()
                .keyword("start")
                .aliases(new String[]{"joined", "tracked"})
                .children(new Command[]{
                        new TributeStartGetCommand(),
                        new TributeStartSetCommand(),
                        new TributeStartUnsetCommand()
                })
                .description("displays and configures the timestamp from which the tribute timeout is checked")
                .details("By default, this command will behave the same as **tribute start get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }
}
