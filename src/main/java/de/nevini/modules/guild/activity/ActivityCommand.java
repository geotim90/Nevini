package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import org.springframework.stereotype.Component;

@Component
public class ActivityCommand extends Command {

    protected ActivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("activity")
                .children(new Command[]{
                        new ActivityUserCommand(),
                        new ActivityGameCommand()
                })
                .module(Module.GUILD)
                .description("displays user activity information")
                .syntax("[ ( --user | -u ) <user> | --game <game> ]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
