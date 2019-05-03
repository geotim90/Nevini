package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class UnsetTimeoutCommand extends Command {

    protected UnsetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .module(Module.LEGACY)
                .node(Node.LEGACY_UNSET_TIMEOUT)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("removes timeouts")
                .syntax("(joined|lastOnline|lastMessage)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
