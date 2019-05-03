package de.nevini.modules.core.module;

import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.command.CommandWithRequiredArgument;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleActivateCommand extends CommandWithRequiredArgument {

    public ModuleActivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("activate")
                .aliases(new String[]{"add", "enable", "+"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_ACTIVATE)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("activates modules")
                .syntax("<module>")
                .build(), "a module");
    }

    @Override
    protected void acceptArgument(CommandEvent event, Message message, String argument) {
        List<Module> modules = event.getModuleService().findModules(argument).stream()
                .sorted(Comparator.comparing(Module::ordinal)).collect(Collectors.toList());
        if (modules.isEmpty()) {
            event.reply(CommandReaction.WARNING, "I could not find any modules that matched your input.");
        } else if (modules.size() > 1) {
            // TODO display options to choose from
            event.reply(CommandReaction.WARNING,
                    "Too many modules matched your input. Please be more specific.");
        } else {
            activateModule(event, modules.get(0));
        }
    }

    private void activateModule(CommandEvent event, Module module) {
        if (Module.CORE.equals(module)) {
            event.reply(CommandReaction.NEUTRAL,
                    "There is no need to activate the core module - it is always active.");
        } else if (event.getModuleService().isModuleActive(event.getGuild(), module)) {
            event.reply(CommandReaction.NEUTRAL, "There is no need to activate the " + module.getName()
                    + " module - it is already active.");
        } else {
            event.getModuleService().setModuleActive(event.getGuild(), module, true);
            event.reply(CommandReaction.OK);
        }
    }

}
