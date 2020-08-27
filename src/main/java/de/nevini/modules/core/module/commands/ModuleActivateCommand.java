package de.nevini.modules.core.module.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Module;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;

class ModuleActivateCommand extends Command {

    ModuleActivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("activate")
                .aliases(new String[]{"add", "enable", "+"})
                .node(Node.CORE_MODULE_ACTIVATE)
                .description("activates a module")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MODULE.describe(false, true)
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
