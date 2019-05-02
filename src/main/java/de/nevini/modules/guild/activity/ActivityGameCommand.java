package de.nevini.modules.guild.activity;

import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.command.CommandWithRequiredArgument;
import de.nevini.db.game.GameData;
import de.nevini.modules.Module;
import de.nevini.modules.Node;
import de.nevini.util.Constants;
import de.nevini.util.FormatUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivityGameCommand extends CommandWithRequiredArgument {

    public ActivityGameCommand() {
        super(CommandDescriptor.builder()
                        .keyword("--game")
                        .module(Module.GUILD)
                        .node(Node.GUILD_ACTIVITY_GAME)
                        .description("displays game activity information")
                        .syntax("<game>")
                        .build(),
                "a game");
    }

    @Override
    protected void acceptArgument(CommandEvent event, Message message, String argument) {
        List<GameData> games = event.getGameService().findGames(argument).stream()
                .sorted(Comparator.comparing(GameData::getName)).collect(Collectors.toList());
        if (games.isEmpty()) {
            event.reply(CommandReaction.WARNING, "I could not find any games that matched your input.");
        } else if (games.size() > 1) {
            // TODO display options to choose from
            event.reply(CommandReaction.WARNING, "Too many games matched your input. Please be more specific.");
        } else {
            reportActivity(event, games.get(0));
        }
    }

    private void reportActivity(CommandEvent event, GameData game) {
        Map<Member, Long> lastPlayed = getLastPlayed(event, game);
        if (lastPlayed.isEmpty()) {
            event.reply("Nobody here has played this game recently.");
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(event.getGuild().getSelfMember().getColor());
            builder.setAuthor(game.getName(), null, game.getIcon());
            lastPlayed.entrySet().stream().limit(Constants.EMBED_MAX_FIELDS).forEach(e ->
                    builder.addField(e.getKey().getEffectiveName(), FormatUtils.formatLargestUnitAgo(e.getValue()),
                            true));
            event.reply(builder.build());
        }
    }

    private Map<Member, Long> getLastPlayed(CommandEvent event, GameData game) {
        Map<Member, Long> result = new LinkedHashMap<>();
        event.getActivityService().getActivityPlaying(game).entrySet().stream().sorted(comparingValueReversed())
                .forEach(e -> {
                    Member member = event.getGuild().getMemberById(e.getKey());
                    if (member != null) result.put(member, e.getValue());
                });
        return result;
    }

    private Comparator<Map.Entry<Long, Long>> comparingValueReversed() {
        Comparator<Map.Entry<Long, Long>> comparator = Comparator.comparing(Map.Entry::getValue);
        return comparator.reversed();
    }

}
