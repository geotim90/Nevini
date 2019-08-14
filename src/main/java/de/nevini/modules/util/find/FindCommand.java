package de.nevini.modules.util.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class FindCommand extends Command {

    public FindCommand() {
        super(CommandDescriptor.builder()
                .keyword("find")
                .aliases(new String[]{"search", "resolve"})
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
