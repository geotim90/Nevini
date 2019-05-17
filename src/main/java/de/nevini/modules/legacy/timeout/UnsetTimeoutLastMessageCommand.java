package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class UnsetTimeoutLastMessageCommand extends Command {

    public UnsetTimeoutLastMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastMessage")
                .aliases(new String[]{"last-message", "message"})
                .node(Node.LEGACY_SET_TIMEOUT)
                .description("removes the lastMessage timeout")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getLegacyTimeoutService().removeLastMessageTimeout(event.getGuild());
        event.reply(CommandReaction.OK, "Removed timeout for **lastMessage**.");
    }

}
