package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class SetTimeoutCommand extends Command {

    protected SetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .module(Module.LEGACY)
                .node(Node.LEGACY_SET_TIMEOUT)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("configures timeouts in days")
                .syntax("(joined|lastOnline|lastMessage) <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
