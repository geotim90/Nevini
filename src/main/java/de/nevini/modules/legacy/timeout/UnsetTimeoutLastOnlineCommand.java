package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;

public class UnsetTimeoutLastOnlineCommand extends Command {

    public UnsetTimeoutLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastOnline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.LEGACY_SET_TIMEOUT)
                .description("removes the lastOnline timeout")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getLegacyTimeoutService().removeLastMessageTimeout(event.getGuild());
        event.reply(CommandReaction.OK, "Removed timeout for **lastOnline**.");
    }

}
