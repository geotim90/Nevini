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

public class ModuleDeactivateCommand extends CommandWithRequiredArgument {

    public ModuleDeactivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("deactivate")
                .aliases(new String[]{"disable", "remove", "-"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_DEACTIVATE)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("deactivates modules")
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
            deactivateModule(event, modules.get(0));
        }
    }

    private void deactivateModule(CommandEvent event, Module module) {
        if (Module.CORE.equals(module)) {
            event.reply(CommandReaction.ERROR, "You cannot deactivate the core module - it is always active.");
        } else if (!event.getModuleService().isModuleActive(event.getGuild(), module)) {
            event.reply(CommandReaction.NEUTRAL, "There is no need to deactivate the " + module.getName()
                    + " module - it is already inactive.");
        } else {
            event.getModuleService().setModuleActive(event.getGuild(), module, false);
            event.reply(CommandReaction.OK);
        }
    }

}
