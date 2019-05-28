package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.common.GameResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindGameCommand extends Command {

    public FindGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .node(Node.GUILD_FIND_GAME)
                .description("finds games by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        GameResolver.describe().build()
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
