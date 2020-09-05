package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;

class ActivitySetCommand extends Command {

    ActivitySetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .children(new Command[]{
                        new ActivitySetOnlineCommand(),
                        new ActivitySetMessageCommand(),
                        new ActivitySetPlayingCommand(),
                        new ActivitySetAwayCommand()
                })
                .description("configures user activity information")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
