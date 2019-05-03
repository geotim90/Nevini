package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ActivityCommand extends Command {

    public ActivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("activity")
                .children(new Command[]{
                        new ActivityUserCommand(),
                        new ActivityGameCommand()
                })
                .module(Module.GUILD)
                .description("displays user activity information")
                .syntax("[ [--user|-u] <user> | --game <game> ]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgument())) {
            event.setArgument(event.getAuthor().getAsMention());
        }
        getChildren()[0].onEvent(event);
    }

}
