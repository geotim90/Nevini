package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import net.dv8tion.jda.core.Permission;

public class GetTimeoutCommand extends Command {

    public GetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .module(Module.LEGACY)
                .node(Node.LEGACY_GET_TIMEOUT)
                .defaultUserPermissions(new Permission[]{Permission.MANAGE_SERVER})
                .description("displays timeouts in days")
                .syntax("(joined|lastOnline|lastMessage)")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
