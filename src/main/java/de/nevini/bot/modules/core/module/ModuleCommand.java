package de.nevini.bot.modules.core.module;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import org.springframework.stereotype.Component;

@Component
public class ModuleCommand extends Command {

    public ModuleCommand() {
        super(CommandDescriptor.builder()
                .keyword("module")
                .aliases(new String[]{"modules"})
                .children(new Command[]{
                        new ModuleGetCommand(),
                        new ModuleActivateCommand(),
                        new ModuleDeactivateCommand()
                })
                .description("displays and configures modules")
                .details("By default, this command will behave the same as **module get**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
