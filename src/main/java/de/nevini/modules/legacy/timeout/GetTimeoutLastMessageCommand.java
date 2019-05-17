package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import de.nevini.util.Formatter;

import java.util.Optional;

public class GetTimeoutLastMessageCommand extends Command {

    public GetTimeoutLastMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastMessage")
                .aliases(new String[]{"last-message", "message"})
                .node(Node.LEGACY_GET_TIMEOUT)
                .description("displays the lastMessage timeout")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Optional<Long> millis = event.getLegacyTimeoutService().getLastMessageTimeout(event.getGuild());
        if (millis.isPresent()) {
            event.reply("Timeout for **lastMessage** is **" + Formatter.formatUnits(millis.get()) + "**.", event::complete);
        } else {
            event.reply("Timeout for **lastMessage** is **undefined**.", event::complete);
        }
    }

}
