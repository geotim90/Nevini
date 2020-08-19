package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

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
