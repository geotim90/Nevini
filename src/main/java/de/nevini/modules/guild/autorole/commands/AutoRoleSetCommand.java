package de.nevini.modules.guild.autorole.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleSetCommand extends Command {

    AutoRoleSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .aliases(new String[]{"add", "start", "+"})
                .children(new Command[]{
                        new AutoRoleSetJoinCommand(),
                        new AutoRoleSetPlayingCommand(),
                        new AutoRoleSetPlaysCommand(),
                        new AutoRoleSetVeteranCommand(),
                        new AutoRoleSetVoiceCommand()
                })
                .description("configures auto-roles")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegateToChildren(event);
    }

}
