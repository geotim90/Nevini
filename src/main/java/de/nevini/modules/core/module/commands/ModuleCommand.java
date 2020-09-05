package de.nevini.modules.core.module.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
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
