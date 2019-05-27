package de.nevini.modules.guild.find;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class FindGameCommand extends Command {

    private static final StringResolver nameResolver = new StringResolver("name or ID", "name");

    public FindGameCommand() {
        super(CommandDescriptor.builder()
                .keyword("game")
                .node(Node.GUILD_FIND_GAME)
                .description("finds games by any of their identifiers")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--name] <name>")
                                .description("Any part of the name or ID of games to look for. The flag is optional if this option is provided first.")
                                .keyword("--name")
                                .aliases(new String[]{"//name"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        nameResolver.resolveArgumentOrOptionOrInput(event, name -> acceptName(event, name));
    }

    private void acceptName(CommandEvent event, String name) {
        List<GameData> games = Resolvers.GAME.findSorted(event, name);
        if (games.isEmpty()) {
            event.reply("I could not find any games that matched your input.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            games.forEach(game -> embed.addField(game.getName(), Long.toString(game.getId()), true));
            event.reply(embed, event::complete);
        }
    }

}
