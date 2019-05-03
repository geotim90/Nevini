package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;

public class ModuleDeactivateCommand extends Command {

    protected ModuleDeactivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("deactivate")
                .aliases(new String[]{"disable", "remove", "-"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_DEACTIVATE)
                .description("deactivates a module")
                .syntax("<module>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
