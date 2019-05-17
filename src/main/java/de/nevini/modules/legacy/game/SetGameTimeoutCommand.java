package de.nevini.modules.legacy.game;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.db.game.GameData;
import de.nevini.modules.Node;
import de.nevini.resolvers.Resolvers;
import de.nevini.util.Formatter;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

public class SetGameTimeoutCommand extends Command {

    public SetGameTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.LEGACY_SET_GAME_TIMEOUT)
                .description("configures the lastPlayed timeout for a game in days")
                .syntax("timeout <game> <days>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String argument = event.getArgument();
        if (StringUtils.isEmpty(argument)) {
            event.reply(CommandReaction.WARNING, "You did not provide a number of days!", event::complete);
        } else {
            String[] args = argument.split("\\s+");
            String days = args[args.length - 1];
            try {
                long millis = TimeUnit.DAYS.toMillis(Integer.parseInt(days));
                if (millis > 0) {
                    Resolvers.GAME.resolveArgumentOrOptionOrInput(event, game -> acceptGame(event, game, millis));
                } else {
                    event.reply(CommandReaction.WARNING, "You did not provide a number of days!", event::complete);
                }
            } catch (NumberFormatException e) {
                event.reply(CommandReaction.WARNING, "You did not provide a number of days!", event::complete);
            }
        }
    }

    private void acceptGame(CommandEvent event, GameData game, long millis) {
        event.getLegacyTimeoutService().setLastPlayedTimeout(event.getGuild(), game, millis);
        event.reply(CommandReaction.OK, "Set timeout for **" + game.getName() + "** to " + Formatter.formatUnits(millis), event::complete);
    }

}
