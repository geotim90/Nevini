package de.nevini.modules.core.module;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class ModuleGetCommand extends Command {

    public ModuleGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .module(Module.CORE)
                .node(Node.CORE_MODULE_GET)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("displays the currently active and inactive modules")
                .syntax("[<module>]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
