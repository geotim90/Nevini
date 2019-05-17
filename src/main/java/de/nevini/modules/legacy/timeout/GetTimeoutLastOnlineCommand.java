package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import de.nevini.util.Formatter;

import java.util.Optional;

public class GetTimeoutLastOnlineCommand extends Command {

    public GetTimeoutLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastOnline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.LEGACY_GET_TIMEOUT)
                .description("displays the lastOnline timeout")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Optional<Long> millis = event.getLegacyTimeoutService().getLastOnlineTimeout(event.getGuild());
        if (millis.isPresent()) {
            event.reply("Timeout for **lastOnline** is **" + Formatter.formatUnits(millis.get()) + "**.", event::complete);
        } else {
            event.reply("Timeout for **lastOnline** is **undefined**.", event::complete);
        }
    }

}
