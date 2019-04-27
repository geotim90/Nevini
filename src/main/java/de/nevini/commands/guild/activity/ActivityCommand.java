package de.nevini.commands.guild.activity;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import de.nevini.bot.AbstractCommand;
import de.nevini.bot.EventDispatcher;
import de.nevini.commands.guild.GuildCategory;
import de.nevini.services.ActivityService;
import de.nevini.services.GameService;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ActivityCommand extends AbstractCommand {

    private final EventDispatcher eventDispatcher;
    private final ActivityService activityService;
    private final GameService gameService;

    protected ActivityCommand(
            @Autowired GuildCategory category,
            @Autowired EventDispatcher eventDispatcher,
            @Autowired ActivityService activityService,
            @Autowired GameService gameService
    ) {
        super("activity", "displays user activity information", category);
        this.eventDispatcher = eventDispatcher;
        this.activityService = activityService;
        this.gameService = gameService;
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
            builder.addUsers(event.getAuthor());
            if (event.getGuild() != null) builder.setColor(event.getSelfMember().getColor());
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
        builder.appendDescription("**Last online**: " + formatUts(activityService.getActivityOnline(member.getUser())));
        builder.appendDescription("\n**Last message**: " + formatUts(activityService.getActivityMessage(member)));
        Map<Long, Long> lastPlayed = activityService.getActivityPlaying(member.getUser());
        if (!lastPlayed.isEmpty()) {
            builder.appendDescription("\n**Last played**:");
            lastPlayed.forEach((id, uts) -> builder.addField(gameService.getGameName(id), formatUts(uts), true));
        }
        event.reply(builder.build());
    }

    private String formatUts(long uts) {
        if (uts <= 0) {
            return "unknown";
        }

        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime then = LocalDateTime.ofInstant(Instant.ofEpochMilli(uts), ZoneOffset.UTC);

        long years = then.until(now, ChronoUnit.YEARS);
        if (years > 1) {
            return years + " years ago";
        } else if (years > 0) {
            return years + " year ago";
        }

        long months = then.until(now, ChronoUnit.MONTHS);
        if (months > 1) {
            return months + " months ago";
        } else if (months > 0) {
            return months + " month ago";
        }

        long weeks = then.until(now, ChronoUnit.WEEKS);
        if (weeks > 1) {
            return weeks + " weeks ago";
        } else if (weeks > 0) {
            return weeks + " week ago";
        }

        long days = then.until(now, ChronoUnit.DAYS);
        if (days > 1) {
            return days + " days ago";
        } else if (days > 0) {
            return "yesterday";
        }

        long hours = then.until(now, ChronoUnit.HOURS);
        if (hours > 1) {
            return hours + " hours ago";
        } else if (hours > 0) {
            return hours + " hour ago";
        }

        long minutes = then.until(now, ChronoUnit.MINUTES);
        if (minutes > 1) {
            return minutes + " minutes ago";
        } else {
            return "just now";
        }
    }

}
