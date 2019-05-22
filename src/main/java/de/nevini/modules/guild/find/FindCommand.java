package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class FindCommand extends Command {

    public FindCommand() {
        super(CommandDescriptor.builder()
                .keyword("find")
                .aliases(new String[]{"search"})
                .children(new Command[]{
                        new FindUserCommand()
                })
                .description("finds users by any of their identifiers")
                .details("By default, this command will behave the same as **find user**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
