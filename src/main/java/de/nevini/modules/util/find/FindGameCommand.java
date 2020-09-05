package de.nevini.modules.util.find;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

class FindGameCommand extends Command {

    FindGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .aliases(new String[]{"games"})
                .guildOnly(false)
                .node(Node.UTIL_FIND_GAME)
                .description("finds games by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.GAME.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.GAME.resolveListArgumentOrOptionOrInput(event, games -> acceptGames(event, games));
    }

    private void acceptGames(CommandEvent event, List<GameData> games) {
        if (games.isEmpty()) {
            event.reply("I could not find any games that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            games.forEach(game -> embed.addField(game.getName(), Long.toString(game.getId()), true));
            event.reply(embed, event::complete);
        }
    }

}
