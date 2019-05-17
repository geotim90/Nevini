package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.modules.Node;
import de.nevini.util.Formatter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

public class SetTimeoutLastOnlineCommand extends Command {

    public SetTimeoutLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastOnline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.LEGACY_SET_TIMEOUT)
                .description("configures the lastOnline timeout in days")
                .syntax("<days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String argument = event.getArgument();
        if (StringUtils.isEmpty(argument)) {
            event.reply(CommandReaction.WARNING, "You did not provide a number of days!", event::complete);
        } else {
            try {
                long millis = TimeUnit.DAYS.toMillis(Integer.parseInt(argument));
                if (millis > 0) {
                    event.getLegacyTimeoutService().setLastOnlineTimeout(event.getGuild(), millis);
                    event.reply(CommandReaction.OK, "Set timeout for **lastOnline** to **" + Formatter.formatUnits(millis) + "**.");
                } else {
                    event.reply(CommandReaction.WARNING, "You did not provide a number of days!", event::complete);
                }
            } catch (NumberFormatException e) {
                event.reply(CommandReaction.WARNING, "You did not provide a number of days!", event::complete);
            }
        }
    }

}
