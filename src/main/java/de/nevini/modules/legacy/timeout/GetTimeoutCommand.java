package de.nevini.modules.legacy.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.db.legacy.timeout.LegacyTimeoutData;
import de.nevini.modules.Node;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class GetTimeoutCommand extends Command {

    public GetTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .children(new Command[]{
                        new GetTimeoutContributionCommand(),
                        new GetTimeoutLastOnlineCommand(),
                        new GetTimeoutLastMessageCommand()
                })
                .node(Node.LEGACY_GET_TIMEOUT)
                .description("displays timeouts")
                .syntax("[contribution|lastOnline|lastMessage]")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = event.createEmbedBuilder();
        embed.addField("contribution", getContributionTimeout(event), true);
        embed.addField("lastOnline", getLastOnlineTimeout(event), true);
        embed.addField("lastMessage", getLastMessageTimeout(event), true);
        List<LegacyTimeoutData> timeouts = event.getLegacyTimeoutService().getLastPlayedTimeouts(event.getGuild());
        if (timeouts.isEmpty()) {
            embed.addField("lastPlayed", "undefined", true);
        } else {
            timeouts.forEach(timeout -> embed.addField(event.getGameService().getGameName(timeout.getId()), Formatter.formatUnits(timeout.getValue()), true));
        }
        event.reply(embed, event::complete);
    }

    private String getContributionTimeout(CommandEvent event) {
        return event.getLegacyTimeoutService().getContributionTimeout(event.getGuild()).map(Formatter::formatUnits).orElse("undefined");
    }

    private String getLastOnlineTimeout(CommandEvent event) {
        return event.getLegacyTimeoutService().getLastOnlineTimeout(event.getGuild()).map(Formatter::formatUnits).orElse("undefined");
    }

    private String getLastMessageTimeout(CommandEvent event) {
        return event.getLegacyTimeoutService().getLastMessageTimeout(event.getGuild()).map(Formatter::formatUnits).orElse("undefined");
    }

}
