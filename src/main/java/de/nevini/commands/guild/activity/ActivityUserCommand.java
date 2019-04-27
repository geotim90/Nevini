package de.nevini.commands.guild.activity;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import de.nevini.bot.EventDispatcher;
import de.nevini.services.ActivityService;
import de.nevini.services.GameService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static de.nevini.util.TimestampUtils.formatLargestUnitAgo;

public class ActivityUserCommand extends Command {

    private final EventDispatcher eventDispatcher;
    private final ActivityService activityService;
    private final GameService gameService;

    public ActivityUserCommand(
            EventDispatcher eventDispatcher,
            ActivityService activityService,
            GameService gameService
    ) {
        this.eventDispatcher = eventDispatcher;
        this.activityService = activityService;
        this.gameService = gameService;

        this.name = "--user";
        this.help = "displays user activity information";
        this.arguments = "[user]";
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION};
    }

    @Override
    protected void execute(CommandEvent event) {
        String query = StringUtils.defaultIfEmpty(event.getArgs(), event.getMessage().getAuthor().getAsMention());
        List<Member> members = FinderUtil.findMembers(query, event.getGuild()).stream().filter(m -> !m.getUser().isBot()).collect(Collectors.toList());
        if (members.isEmpty()) {
            event.replyWarning("I could not find any users that matched your input.");
        } else if (members.size() > 10) {
            event.replyWarning("Too many users matched your input. Please be more specific.");
        } else if (members.size() > 1) {
            OrderedMenu.Builder builder = new OrderedMenu.Builder();
            builder.setEventWaiter(eventDispatcher.getEventWaiter());
            builder.setUsers(event.getAuthor());
            builder.setColor(event.getSelfMember().getColor());
            builder.setDescription("Please choose one of the following users.\n");
            members.stream().map(Member::getEffectiveName).forEach(builder::addChoice);
            builder.setSelection((msg, num) -> reportActivity(event, members.get(num - 1)));
            builder.build().display(event.getMessage().getChannel());
        } else {
            reportActivity(event, members.get(0));
        }
    }

    private void reportActivity(CommandEvent event, Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getSelfMember().getColor());
        builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
        builder.setTitle("Recent activity");
        builder.appendDescription("**Last online**: " + formatLargestUnitAgo(activityService.getActivityOnline(member.getUser())));
        builder.appendDescription("\n**Last message**: " + formatLargestUnitAgo(activityService.getActivityMessage(member)));
        Map<Long, Long> lastPlayed = getLastPlayed(member);
        if (!lastPlayed.isEmpty()) {
            builder.appendDescription("\n**Last played**:");
            lastPlayed.forEach((id, uts) -> builder.addField(gameService.getGameName(id), formatLargestUnitAgo(uts), true));
        }
        event.reply(builder.build());
    }

    private Map<Long, Long> getLastPlayed(Member member) {
        Map<Long, Long> result = new LinkedHashMap<>();
        activityService.getActivityPlaying(member.getUser()).entrySet().stream().sorted(comparingValueReversed()).forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    private Comparator<Map.Entry<Long, Long>> comparingValueReversed() {
        Comparator<Map.Entry<Long, Long>> comparator = Comparator.comparing(Map.Entry::getValue);
        return comparator.reversed();
    }

}
