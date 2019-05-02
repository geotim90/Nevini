package de.nevini.modules.guild.activity;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandReaction;
import de.nevini.command.CommandWithRequiredArgument;
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

public class ActivityUserCommand extends CommandWithRequiredArgument {

    protected ActivityUserCommand() {
        super(CommandDescriptor.builder()
                        .keyword("--user")
                        .aliases(new String[]{"-u"})
                        .module(Module.GUILD)
                        .node(Node.GUILD_ACTIVITY_USER)
                        .description("displays user activity information")
                        .syntax("<user>")
                        .build(),
                "a user");
    }

    @Override
    protected void acceptArgument(CommandEvent event, Message message, String argument) {
        List<Member> members = FinderUtil.findMembers(argument, event.getGuild()).stream().filter(m ->
                !m.getUser().isBot()).collect(Collectors.toList());
        if (members.isEmpty()) {
            event.reply(CommandReaction.WARNING, "I could not find any users that matched your input.");
        } else if (members.size() > 1) {
            // TODO display options to choose from
            event.reply(CommandReaction.WARNING, "Too many users matched your input. Please be more specific.");
        } else {
            reportActivity(event, members.get(0));
        }
    }

    private void reportActivity(CommandEvent event, Member member) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setAuthor(member.getEffectiveName(), null, member.getUser().getAvatarUrl());
        builder.addField("Discord", FormatUtils.formatLargestUnitAgo(
                event.getActivityService().getActivityOnline(member.getUser())), true);
        builder.addField(event.getGuild().getName(), FormatUtils.formatLargestUnitAgo(
                event.getActivityService().getActivityMessage(member)), true);
        getLastPlayed(event, member).forEach((id, timestamp) -> builder.addField(event.getGameService().getGameName(id),
                FormatUtils.formatLargestUnitAgo(timestamp), true));
        event.reply(builder.build());
    }

    private Map<Long, Long> getLastPlayed(CommandEvent event, Member member) {
        Map<Long, Long> result = new LinkedHashMap<>();
        event.getActivityService().getActivityPlaying(member.getUser()).entrySet().stream()
                .sorted(comparingValueReversed()).limit(Constants.EMBED_MAX_FIELDS - 2)
                .forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    private Comparator<Map.Entry<Long, Long>> comparingValueReversed() {
        Comparator<Map.Entry<Long, Long>> comparator = Comparator.comparing(Map.Entry::getValue);
        return comparator.reversed();
    }

}
