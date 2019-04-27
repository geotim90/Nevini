package de.nevini.commands.guild.activity;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import com.jagrosh.jdautilities.menu.Paginator;
import de.nevini.bot.EventDispatcher;
import de.nevini.db.game.GameData;
import de.nevini.services.ActivityService;
import de.nevini.services.GameService;
import de.nevini.util.TimestampUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ActivityGameCommand extends Command {

    private final EventDispatcher eventDispatcher;
    private final ActivityService activityService;
    private final GameService gameService;

    public ActivityGameCommand(
            EventDispatcher eventDispatcher,
            ActivityService activityService,
            GameService gameService
    ) {
        this.eventDispatcher = eventDispatcher;
        this.activityService = activityService;
        this.gameService = gameService;

        this.name = "--game";
        this.help = "displays user activity information for a specific game";
        this.arguments = "<game>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION};
    }

    @Override
    protected void execute(CommandEvent event) {
        String query = event.getArgs();
        if (StringUtils.isEmpty(query)) {
            event.replyWarning("Too many games matched your input. Please be more specific.");
        } else {
            List<GameData> games = gameService.findGames(query).stream().sorted(Comparator.comparing(GameData::getName)).collect(Collectors.toList());
            if (games.isEmpty()) {
                event.replyWarning("I could not find any games that matched your input.");
            } else if (games.size() > 10) {
                event.replyWarning("Too many games matched your input. Please be more specific.");
            } else if (games.size() > 1) {
                OrderedMenu.Builder builder = new OrderedMenu.Builder();
                builder.setEventWaiter(eventDispatcher.getEventWaiter());
                builder.setUsers(event.getAuthor());
                builder.setColor(event.getSelfMember().getColor());
                builder.setDescription("Please choose one of the following games.\n");
                games.stream().map(GameData::getName).forEach(builder::addChoice);
                builder.setSelection((msg, num) -> reportActivity(event, games.get(num - 1)));
                builder.build().display(event.getMessage().getChannel());
            } else {
                reportActivity(event, games.get(0));
            }
        }
    }

    private void reportActivity(CommandEvent event, GameData game) {
        Map<Member, Long> lastPlayed = getLastPlayed(event, game);
        if (lastPlayed.isEmpty()) {
            event.reply("Nobody here has played this game recently.");
        } else {
            Paginator.Builder builder = new Paginator.Builder();
            builder.setEventWaiter(eventDispatcher.getEventWaiter());
            builder.setUsers(event.getAuthor());
            builder.setColor(event.getSelfMember().getColor());
            builder.setText("Recent activity - last played **" + game.getName() + "**:");
            lastPlayed.forEach((k, v) -> builder.addItems("**" + k.getEffectiveName() + "** " + TimestampUtils.formatLargestUnitAgo(v)));
            builder.build().display(event.getChannel());
        }
    }

    private Map<Member, Long> getLastPlayed(CommandEvent event, GameData game) {
        Map<Member, Long> result = new LinkedHashMap<>();
        activityService.getActivityPlaying(game).entrySet().stream().sorted(comparingValueReversed()).forEach(e -> {
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
