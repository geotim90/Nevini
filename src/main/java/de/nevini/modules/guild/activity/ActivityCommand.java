package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.db.game.GameData;
import de.nevini.modules.Module;
import de.nevini.resolvers.GameResolver;
import de.nevini.resolvers.MemberResolver;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ActivityCommand extends Command {

    private final GameResolver gameResolver = new GameResolver();
    private final MemberResolver memberResolver = new MemberResolver();

    public ActivityCommand() {
        super(CommandDescriptor.builder()
                .keyword("activity")
                .module(Module.GUILD)
                .description("displays user activity information")
                .syntax("[--user] [<user>] --game <game>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        memberResolver.resolveArgumentOrOptionOrDefault(event, event.getMember(), this::acceptMember);
    }

    private void acceptMember(CommandEvent event, Message message, Member member) {
        gameResolver.resolveOptionIfExists(event, (e, m, game) -> acceptMemberGame(e, m, member, game));
    }

    private void acceptMemberGame(CommandEvent event, Message message, Member member, GameData game) {
        if (member == null && game == null) {
            event.replyTo(message, CommandReaction.WARNING, "I could not find anything that matched your input!");
        } else if (game == null) {
            reportUserActivity(event, member);
        } else if (member == null) {
            reportGameActivity(event, game);
        } else {
            reportUserGameActivity(event, member, game);
        }
    }

    private void reportUserActivity(CommandEvent event, Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
        builder.addField("Discord", Formatter.formatLargestUnitAgo(
                event.getActivityService().getActivityOnline(member.getUser())), true);
        builder.addField(event.getGuild().getName(), Formatter.formatLargestUnitAgo(
                event.getActivityService().getActivityMessage(member)), true);
        getLastPlayed(event, member).forEach((id, timestamp) -> builder.addField(event.getGameService().getGameName(id),
                Formatter.formatLargestUnitAgo(timestamp), true));
        event.reply(builder);
    }

    private Map<Long, Long> getLastPlayed(CommandEvent event, Member member) {
        Map<Long, Long> result = new LinkedHashMap<>();
        event.getActivityService().getActivityPlaying(member.getUser()).entrySet().stream()
                .sorted(comparingValueReversed()).forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    private void reportGameActivity(CommandEvent event, GameData game) {
        Map<Member, Long> lastPlayed = getLastPlayed(event, game);
        if (lastPlayed.isEmpty()) {
            event.reply("Nobody here has played this game recently.");
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(event.getGuild().getSelfMember().getColor());
            builder.setAuthor(game.getName(), null, game.getIcon());
            lastPlayed.forEach((member, timestamp) -> builder.addField(member.getEffectiveName(),
                    Formatter.formatLargestUnitAgo(timestamp), true));
            event.reply(builder);
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

    private void reportUserGameActivity(CommandEvent event, Member member, GameData game) {
        Long lastPlayed = event.getActivityService().getActivityPlaying(member.getUser(), game);
        if (lastPlayed == null) {
            event.reply(member.getEffectiveName() + " has not played this game recently.");
        } else {
            event.reply(member.getEffectiveName() + " played this game " + Formatter.formatLargestUnitAgo(lastPlayed));
        }
    }

    private Comparator<Map.Entry<Long, Long>> comparingValueReversed() {
        Comparator<Map.Entry<Long, Long>> comparator = Comparator.comparing(Map.Entry::getValue);
        return comparator.reversed();
    }

}
