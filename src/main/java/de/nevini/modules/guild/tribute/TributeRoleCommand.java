package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;

class TributeRoleCommand extends Command {

    TributeRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("role")
                .children(new Command[]{
                        new TributeRoleGetCommand(),
                        new TributeRoleSetCommand(),
                        new TributeRoleUnsetCommand()
                })
                .description("displays and configures the role for users that need to contribute")
                .details("By default, this command will behave the same as **tribute role get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
