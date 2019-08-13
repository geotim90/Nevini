package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

class InactivityUnsetCommand extends Command {

    InactivityUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove"})
                .children(new Command[]{
                        new InactivityUnsetOnlineCommand(),
                        new InactivityUnsetMessageCommand(),
                        new InactivityUnsetPlayingCommand(),
                })
                .description("removes user inactivity thresholds")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
