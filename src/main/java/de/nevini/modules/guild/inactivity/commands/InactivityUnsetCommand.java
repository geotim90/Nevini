package de.nevini.modules.guild.inactivity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

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
