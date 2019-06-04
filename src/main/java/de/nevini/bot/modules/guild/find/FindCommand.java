package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class FindCommand extends Command {

    public FindCommand() {
        super(CommandDescriptor.builder()
                .keyword("find")
                .aliases(new String[]{"search"})
                .children(new Command[]{
                        new FindUserCommand(),
                        new FindRoleCommand(),
                        new FindPermissionCommand(),
                        new FindNodeCommand(),
                        new FindModuleCommand(),
                        new FindGameCommand(),
                        new FindChannelCommand()
                })
                .description("finds users, roles etc. by any of their identifiers")
                .details("By default, this command will behave the same as **find user**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
