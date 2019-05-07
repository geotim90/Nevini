package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.resolvers.ModuleResolver;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

public class ModuleDeactivateCommand extends Command {

    private final ModuleResolver moduleResolver = new ModuleResolver();

    public ModuleDeactivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("deactivate")
                .aliases(new String[]{"disable", "remove", "-"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_DEACTIVATE)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("deactivates modules")
                .syntax("<module>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        moduleResolver.resolveArgumentOrOptionOrInput(event, this::deactivateModule);
    }

    private void deactivateModule(CommandEvent event, Message message, Module module) {
        if (Module.CORE.equals(module)) {
            event.replyTo(message, CommandReaction.ERROR, "You cannot deactivate the core module - it is always active.");
        } else if (!event.getModuleService().isModuleActive(event.getGuild(), module)) {
            event.replyTo(message, CommandReaction.NEUTRAL, "There is no need to deactivate the " + module.getName() + " module - it is already inactive.");
        } else {
            event.getModuleService().setModuleActive(event.getGuild(), module, false);
            event.replyTo(message, CommandReaction.OK);
        }
    }

}
