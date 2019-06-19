package de.nevini.bot.modules.guild.autorole;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.framework.command.CommandReaction;
import de.nevini.framework.message.PickerEmbed;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
class AutoRoleSetCommand extends Command {

    AutoRoleSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .aliases(new String[]{"add", "start", "+"})
                .children(new Command[]{
                        new AutoRoleSetJoinCommand(),
                        new AutoRoleSetPlayingCommand(),
                        new AutoRoleSetPlaysCommand()
                })
                .description("configures auto-roles")
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
