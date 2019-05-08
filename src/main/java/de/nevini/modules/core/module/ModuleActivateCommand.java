package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;

public class ModuleActivateCommand extends Command {

    public ModuleActivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("activate")
                .aliases(new String[]{"add", "enable", "+"})
                .node(Node.CORE_MODULE_ACTIVATE)
                .description("activates modules")
                .syntax("<module>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MODULE.resolveArgumentOrOptionOrInput(event, module -> activateModule(event, module));
    }

    private void activateModule(CommandEvent event, Module module) {
        if (Module.CORE.equals(module)) {
            event.reply(CommandReaction.DEFAULT_OK, "There is no need to activate the core module - it is always active.");
        } else if (event.getModuleService().isModuleActive(event.getGuild(), module)) {
            event.reply(CommandReaction.DEFAULT_OK, "There is no need to activate the " + module.getName() + " module - it is already active.");
        } else {
            event.getModuleService().setModuleActive(event.getGuild(), module, true);
            event.reply(CommandReaction.OK);
        }
    }

}
