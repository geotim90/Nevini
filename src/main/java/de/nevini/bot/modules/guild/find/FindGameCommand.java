package de.nevini.bot.modules.guild.find;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindGameCommand extends Command {

    public FindGameCommand() {
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