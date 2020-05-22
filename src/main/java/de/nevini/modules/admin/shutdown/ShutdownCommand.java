package de.nevini.modules.admin.shutdown;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
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
