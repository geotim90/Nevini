package de.nevini.bot.modules.guild.inactivity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.framework.command.CommandReaction;
import de.nevini.framework.message.PickerEmbed;

import java.util.Arrays;

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
