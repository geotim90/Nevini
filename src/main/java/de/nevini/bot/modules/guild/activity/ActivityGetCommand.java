package de.nevini.bot.modules.guild.activity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.util.Formatter;
import de.nevini.framework.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

class ActivityGetCommand extends Command {

    ActivityGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .node(Node.GUILD_ACTIVITY_GET)
                .description("displays user and/or game activity information")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.GAME.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(event,
                event.getMember(),
                member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.GAME.resolveOptionOrInputIfExists(event, game -> acceptMemberGame(event, member, game));
    }

    private void acceptMemberGame(CommandEvent event, Member member, GameData game) {
        if (member == null && game == null) {
            reportUserActivity(event, event.getMember());
        } else if (game == null) {
            reportUserActivity(event, member);
        } else if (member == null) {
            reportGameActivity(event, game);
        } else {
            reportUserGameActivity(event, member, game);
        }
    }

    private void reportUserActivity(CommandEvent event, Member member) {
        EmbedBuilder builder = event.createEmbedBuilder();
        builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
        builder.addField("Discord", Formatter.formatLargestUnitAgo(
                ObjectUtils.defaultIfNull(event.getActivityService().getActivityOnline(member), 0L)
        ), true);
        builder.addField(event.getGuild().getName(), Formatter.formatLargestUnitAgo(
                ObjectUtils.defaultIfNull(event.getActivityService().getActivityMessage(member), 0L)
        ), true);
        getLastPlayed(event, member).forEach((id, timestamp) -> builder.addField(event.getGameService().getGameName(id),
                Formatter.formatLargestUnitAgo(timestamp), true));
        event.reply(builder, event::complete);
    }

    private Map<Long, Long> getLastPlayed(CommandEvent event, Member member) {
        Map<Long, Long> result = new LinkedHashMap<>();
        event.getActivityService().getActivityPlaying(member).entrySet().stream()
                .sorted(comparingValueReversed()).forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    private void reportGameActivity(CommandEvent event, GameData game) {
        Map<Member, Long> lastPlayed = getLastPlayed(event, game);
        if (lastPlayed.isEmpty()) {
            event.reply("Nobody here has played this game recently.", event::complete);
        } else {
            EmbedBuilder builder = event.createEmbedBuilder();
            builder.setAuthor(game.getName(), null, game.getIcon());
            lastPlayed.forEach((member, timestamp) -> builder.addField(member.getEffectiveName(),
                    Formatter.formatLargestUnitAgo(timestamp), true));
            event.reply(builder, event::complete);
        }
    }

    private Map<Member, Long> getLastPlayed(CommandEvent event, GameData game) {
        Map<Member, Long> result = new LinkedHashMap<>();
        event.getActivityService().getActivityPlaying(event.getGuild(), game).entrySet().stream()
                .sorted(comparingValueReversed())
                .forEach(e -> {
                    Member member = event.getGuild().getMemberById(e.getKey());
                    if (member != null) result.put(member, e.getValue());
                });
        return result;
    }

    private void reportUserGameActivity(CommandEvent event, Member member, GameData game) {
        Long lastPlayed = event.getActivityService().getActivityPlaying(member, game);
        if (lastPlayed == null) {
            event.reply(member.getEffectiveName() + " has not played this game recently.", event::complete);
        } else {
            event.reply(member.getEffectiveName() + " played this game "
                    + Formatter.formatLargestUnitAgo(lastPlayed), event::complete);
        }
    }

    private Comparator<Map.Entry<Long, Long>> comparingValueReversed() {
        Comparator<Map.Entry<Long, Long>> comparator = Comparator.comparing(Map.Entry::getValue);
        return comparator.reversed();
    }

}