package de.nevini.modules.core.module;

import de.nevini.command.*;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.resolvers.common.ModuleResolver;
import de.nevini.resolvers.common.Resolvers;

public class ModuleActivateCommand extends Command {

    public ModuleActivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("activate")
                .aliases(new String[]{"add", "enable", "+"})
                .node(Node.CORE_MODULE_ACTIVATE)
                .description("activates a module")
                .options(new CommandOptionDescriptor[]{
                        ModuleResolver.describe()
                                .syntax("[--module] <module>")
                                .description("The name (or part of the name) of the module to activate. The flag is optional.")
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MODULE.resolveArgumentOrOptionOrInput(event, module -> activateModule(event, module));
    }

    private void activateModule(CommandEvent event, Module module) {
        if (Module.CORE.equals(module)) {
            event.reply(CommandReaction.DEFAULT_OK,
                    "There is no need to activate the **core** module - it is always active.",
                    event::complete);
        } else if (event.getModuleService().isModuleActive(event.getGuild(), module)) {
            event.reply(CommandReaction.DEFAULT_OK,
                    "There is no need to activate the **" + module.getName() + "** module - it is already active.",
                    event::complete);
        } else {
            event.getModuleService().setModuleActive(event.getGuild(), module, true);
            event.reply(CommandReaction.OK, event::complete);
        }
    }

}
