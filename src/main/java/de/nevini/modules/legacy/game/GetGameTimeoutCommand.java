package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.db.game.GameData;
import de.nevini.db.legacy.timeout.LegacyTimeoutData;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;
import java.util.Optional;

public class GetGameTimeoutCommand extends Command {

    public GetGameTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.LEGACY_GET_GAME_TIMEOUT)
                .description("displays the lastPlayed timeout for a game in days")
                .syntax("<game>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveArgumentOrOptionOrInputIfExists(event, game -> acceptGame(event, game));
    }

    private void acceptGame(CommandEvent event, GameData game) {
        if (game == null) {
            List<LegacyTimeoutData> timeouts = event.getLegacyTimeoutService().getLastPlayedTimeouts(event.getGuild());
            if (timeouts.isEmpty()) {
                event.reply("Timeout for **lastPlayed** is **undefined**.", event::complete);
            } else {
                EmbedBuilder embed = event.createEmbedBuilder();
                timeouts.forEach(timeout -> embed.addField(event.getGameService().getGameName(timeout.getId()), Formatter.formatUnits(timeout.getValue()), true));
                event.reply(embed, event::complete);
            }
        } else {
            Optional<Long> timeout = event.getLegacyTimeoutService().getLastPlayedTimeout(event.getGuild(), game);
            if (timeout.isPresent()) {
                event.reply("Timeout for **" + game.getName() + "** is **" + Formatter.formatUnits(timeout.get()) + "**.", event::complete);
            } else {
                event.reply("Timeout for **" + game.getName() + "** is **undefined**.", event::complete);
            }
        }
    }

}
