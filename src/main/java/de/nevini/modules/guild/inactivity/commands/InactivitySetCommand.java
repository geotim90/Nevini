package de.nevini.modules.guild.inactivity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

class InactivitySetCommand extends Command {

    InactivitySetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .children(new Command[]{
                        new InactivitySetOnlineCommand(),
                        new InactivitySetMessageCommand(),
                        new InactivitySetPlayingCommand(),
                })
                .description("configures user inactivity thresholds")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
