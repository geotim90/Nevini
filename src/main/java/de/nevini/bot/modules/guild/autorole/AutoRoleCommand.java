package de.nevini.bot.modules.guild.autorole;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class AutoRoleCommand extends Command {

    public AutoRoleCommand() {
        super(CommandDescriptor.builder()
                .keyword("auto-role")
                .aliases(new String[]{"auto-roles", "ar"})
                .children(new Command[]{
                        new AutoRoleGetCommand(),
                        new AutoRoleSetCommand(),
                        new AutoRoleUnsetCommand()
                })
                .description("displays and configures auto-roles")
                .details("By default, this command will behave the same as **auto-role get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
