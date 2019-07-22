package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandReaction;
import de.nevini.util.message.PickerEmbed;

import java.util.Arrays;

class ActivitySetCommand extends Command {

    ActivitySetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .children(new Command[]{
                        new ActivitySetOnlineCommand(),
                        new ActivitySetMessageCommand(),
                        new ActivitySetPlayingCommand(),
                })
                .description("configures user activity information")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // let user pick child command to execute
        new PickerEmbed<>(
                event.getTextChannel(),
                event.getAuthor(),
                event.createEmbedBuilder(),
                Arrays.asList(getChildren()),
                Command::getKeyword,
                Command::getDescription,
                event.getEventDispatcher(),
                command -> command.onEvent(event),
                () -> event.reply(CommandReaction.DEFAULT_NOK, event::complete)
        ).display();
    }

}
