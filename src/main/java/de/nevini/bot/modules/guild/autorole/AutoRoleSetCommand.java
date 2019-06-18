package de.nevini.bot.modules.guild.autorole;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class AutoRoleSetCommand extends Command {

    AutoRoleSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .children(new Command[]{
                        new AutoRoleSetJoinCommand(),
                        new AutoRoleSetPlayingCommand(),
                        new AutoRoleSetPlaysCommand()
                })
                .description("configures auto-roles")
                .details("By default, this command will behave the same as **auto-role set join**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
