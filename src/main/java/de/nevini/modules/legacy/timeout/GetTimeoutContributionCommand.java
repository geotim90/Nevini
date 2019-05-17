package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;
import de.nevini.util.Formatter;

import java.util.Optional;

public class GetTimeoutContributionCommand extends Command {

    public GetTimeoutContributionCommand() {
        super(CommandDescriptor.builder()
                .keyword("contribution")
                .node(Node.LEGACY_GET_TIMEOUT)
                .description("displays the contribution timeout")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Optional<Long> millis = event.getLegacyTimeoutService().getContributionTimeout(event.getGuild());
        if (millis.isPresent()) {
            event.reply("Timeout for **contribution** is **" + Formatter.formatUnits(millis.get()) + "**.", event::complete);
        } else {
            event.reply("Timeout for **contribution** is **undefined**.", event::complete);
        }
    }

}
