package de.nevini.modules.core.shutdown;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
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
                .minimumUserPermissions(Permissions.NONE)
                .description("safely shuts down the bot")
                .details("This command can only be executed by the owner of the bot.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(CommandReaction.OK, ignore -> {
            event.complete(true);
            event.getJDA().shutdown();
        });
    }

}
