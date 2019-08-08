package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

class ActivityUnsetCommand extends Command {

    ActivityUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .children(new Command[]{
                        new ActivityUnsetOnlineCommand(),
                        new ActivityUnsetMessageCommand(),
                        new ActivityUnsetPlayingCommand(),
                })
                .description("configures user activity information")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
