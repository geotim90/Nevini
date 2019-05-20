package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;

public class ModuleDeactivateCommand extends Command {

    public ModuleDeactivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("deactivate")
                .aliases(new String[]{"disable", "remove", "-"})
                .node(Node.CORE_MODULE_DEACTIVATE)
                .description("deactivates a module")
                .syntax("[--module] <module>")
                .details("__Options__\n**[--module] <module>** (required) - the module to deactivate")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MODULE.resolveArgumentOrOptionOrInput(event, module -> deactivateModule(event, module));
    }

    private void deactivateModule(CommandEvent event, Module module) {
        if (Module.CORE.equals(module)) {
            event.reply(CommandReaction.ERROR,
                    "You cannot deactivate the **core** module - it is always active.",
                    event::complete);
        } else if (!event.getModuleService().isModuleActive(event.getGuild(), module)) {
            event.reply(CommandReaction.DEFAULT_OK,
                    "There is no need to deactivate the **" + module.getName() + "** module - it is already inactive.",
                    event::complete);
        } else {
            event.getModuleService().setModuleActive(event.getGuild(), module, false);
            event.reply(CommandReaction.OK, event::complete);
        }
    }

}
