package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandReaction;
import de.nevini.util.message.PickerEmbed;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
class AutoRoleUnsetCommand extends Command {

    AutoRoleUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove", "stop", "-"})
                .children(new Command[]{
                        new AutoRoleUnsetJoinCommand(),
                        new AutoRoleUnsetPlayingCommand(),
                        new AutoRoleUnsetPlaysCommand()
                })
                .description("removes auto-roles")
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
