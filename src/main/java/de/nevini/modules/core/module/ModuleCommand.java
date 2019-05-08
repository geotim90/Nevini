package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
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
                .description("configures modes")
                .syntax("[ get [<module>] | (activate|deactivate) <module> ]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        getChildren()[0].onEvent(event);
    }

}
