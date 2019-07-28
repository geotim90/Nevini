package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

class FindGameCommand extends Command {

    FindGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .node(Node.GUILD_FIND_GAME)
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
