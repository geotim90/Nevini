package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

class TributeDelayCommand extends Command {

    TributeDelayCommand() {
        super(CommandDescriptor.builder()
                .keyword("delay")
                .aliases(new String[]{"extend", "postpone"})
                .children(new Command[]{
                        new TributeDelayGetCommand(),
                        new TributeDelaySetCommand(),
                        new TributeDelayUnsetCommand()
                })
                .description("displays and configures delayed tribute timeouts for individual users")
                .details("By default, this command will behave the same as **tribute delay get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }
}
