package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class ModuleActivateCommand extends Command {

    public ModuleActivateCommand() {
        super(CommandDescriptor.builder()
                .keyword("activate")
                .aliases(new String[]{"add", "enable", "+"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_ACTIVATE)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("activates a module")
                .syntax("<module>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
