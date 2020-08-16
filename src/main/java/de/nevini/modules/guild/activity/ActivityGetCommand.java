package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

class ActivityGetCommand extends Command {

    ActivityGetCommand() {
        super(CommandDescriptor.builder()
                .keyword("get")
                .aliases(new String[]{"display", "echo", "list", "print", "show"})
                .children(new Command[]{
                        new ActivityGetMessageCommand(),
                        new ActivityGetOnlineCommand(),
                        new ActivityGetPlayingCommand()
                })
                .node(Node.GUILD_ACTIVITY_GET)
                .description("displays user, role and/or game activity information")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.ROLE.describe(),
                        Resolvers.GAME.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        if (StringUtils.isEmpty(event.getArgument()) && !Resolvers.MEMBER.isOptionPresent(event)
                && Resolvers.ROLE.isOptionPresent(event)) {
            Resolvers.ROLE.resolveOptionOrInput(event, role -> acceptRole(event, role));
        } else {
            Resolvers.MEMBER.resolveArgumentOrOptionOrDefaultIfExists(event,
                    event.getMember(),
                    member -> acceptMember(event, member));
        }
    }

    private void acceptRole(CommandEvent event, Role role) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptRoleGame(event, role, game));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.GAME.resolveOptionOrInputIfExists(event, game -> acceptMemberGame(event, member, game));
    }

    private void acceptRoleGame(CommandEvent event, Role role, GameData game) {
        Map<Member, Long> lastPlayed = getLastPlayed(event, role, game);
        if (lastPlayed.isEmpty()) {
            event.reply("Nobody with that role has played this game recently.", event::complete);
        } else {
            EmbedBuilder builder = event.createGuildEmbedBuilder();
            builder.setAuthor(game.getName(), null, game.getIcon());
            lastPlayed.forEach((member, timestamp) -> builder.addField(member.getEffectiveName(),
                    Formatter.formatLargestUnitAgo(timestamp), true));
            event.reply(builder, event::complete);
        }
    }

    private Map<Member, Long> getLastPlayed(CommandEvent event, Role role, GameData game) {
        Map<Member, Long> result = new LinkedHashMap<>();
        event.getActivityService().getActivityPlaying(event.getGuild(), game).entrySet().stream()
                .sorted(comparingValueReversed())
                .forEach(e -> {
                    Member member = event.getGuild().getMemberById(e.getKey());
                    if (member != null && member.getRoles().contains(role)) {
                        result.put(member, e.getValue());
                    }
                });
        return result;
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
        EmbedBuilder builder = event.createMemberEmbedBuilder(member);
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
            EmbedBuilder builder = event.createGameEmbedBuilder(game);
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
        Long lastPlayed = event.getActivityService().getActivityPlaying(member, game.getId());
        if (lastPlayed == null) {
            event.reply(member.getEffectiveName() + " has not played this game recently.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** last played **" + game.getName() + "** ("
                    + Long.toUnsignedString(game.getId()) + ") **" + Formatter.formatLargestUnitAgo(lastPlayed) + "** ("
                    + Formatter.formatTimestamp(lastPlayed) + ")\n", event::complete);
        }
    }

    private Comparator<Map.Entry<Long, Long>> comparingValueReversed() {
        Comparator<Map.Entry<Long, Long>> comparator = Comparator.comparing(Map.Entry::getValue);
        return comparator.reversed();
    }

}
