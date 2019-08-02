package de.nevini.resolvers.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TextChannelResolver extends OptionResolver<TextChannel> {

    TextChannelResolver() {
        super("channel", new Pattern[]{
                FinderUtil.CHANNEL_MENTION,
                Pattern.compile("(?i)(?:(?:--|//)channel|[-/]c)(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax("[--channel] [<channel>]")
                .description("Refers to " + (list ? "all text channels" : "a specific text channel")
                        + " with a matching mention, id or name.\n"
                        + "The `--channel` flag is optional if a channel mention is used"
                        + (argument ? " or this option is provided first" : "") + ".\n"
                        + "Refers to the current channel if only the `--channel` flag is provided.")
                .keyword("--channel")
                .aliases(new String[]{"//channel", "-c", "/c"})
                .build();
    }

    @Override
    public List<TextChannel> findSorted(@NonNull CommandEvent event, String query) {
        List<TextChannel> matches = FinderUtil.findTextChannels(query, event.getGuild()).stream()
                .sorted(Comparator.comparing(TextChannel::getName))
                .collect(Collectors.toList());

        if (matches.isEmpty() && query.startsWith("#")) {
            return findSorted(event, query.substring(1));
        } else {
            return matches;
        }
    }

    @Override
    protected @NonNull String getFieldNameForPicker(TextChannel item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(TextChannel item) {
        return item.getId();
    }

}
