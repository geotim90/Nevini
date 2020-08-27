package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleUnsetCommand extends Command {

    AutoRoleUnsetCommand() {
        super(CommandDescriptor.builder()
                .keyword("unset")
                .aliases(new String[]{"remove", "stop", "-"})
                .children(new Command[]{
                        new AutoRoleUnsetJoinCommand(),
                        new AutoRoleUnsetPlayingCommand(),
                        new AutoRoleUnsetPlaysCommand(),
                        new AutoRoleUnsetVeteranCommand(),
                        new AutoRoleUnsetVoiceCommand()
                })
                .description("removes auto-roles")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
