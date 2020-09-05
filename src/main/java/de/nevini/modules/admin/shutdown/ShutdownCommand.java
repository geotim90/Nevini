package de.nevini.modules.admin.shutdown;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super(CommandDescriptor.builder()
                .keyword("shutdown")
                .ownerOnly(true)
                .guildOnly(false)
                .node(Node.CORE_HELP) // dummy node
                .minimumBotPermissions(Permissions.NONE)
                .description("safely shuts down the bot")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(CommandReaction.OK, message -> event.getJDA().shutdown());
    }

}
