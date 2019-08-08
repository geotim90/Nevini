package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

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
