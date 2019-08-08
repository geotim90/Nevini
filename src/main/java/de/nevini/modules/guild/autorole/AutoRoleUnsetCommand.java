package de.nevini.modules.guild.autorole;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
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
