package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

class ActivityUnsetCommand extends Command {

    ActivityUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .children(new Command[]{
                        new ActivityUnsetOnlineCommand(),
                        new ActivityUnsetMessageCommand(),
                        new ActivityUnsetPlayingCommand(),
                        new ActivityUnsetAwayCommand()
                })
                .description("configures user activity information")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
