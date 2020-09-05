package de.nevini.core.resolvers.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VoiceChannelResolver extends OptionResolver<VoiceChannel> {

    VoiceChannelResolver() {
        super("voice channel", new Pattern[]{
                FinderUtil.CHANNEL_MENTION,
                Pattern.compile("(?i)(?:(?:--|//)channel|[-/]c)(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax("[--channel] [<channel>]")
                .description("Refers to " + (list ? "all voice channels" : "a specific voice channel")
                        + " with a matching mention, id or name.\n"
                        + "The `--channel` flag is optional if a channel mention is used"
                        + (argument ? " or this option is provided first" : "") + ".\n"
                        + "Refers to the current channel if only the `--channel` flag is provided.")
                .keyword("--channel")
                .aliases(new String[]{"//channel", "-c", "/c"})
                .build();
    }

    @Override
    public List<VoiceChannel> findSorted(@NonNull CommandEvent event, String query) {
        List<VoiceChannel> matches = FinderUtil.findVoiceChannels(query, event.getGuild()).stream()
                .sorted(Comparator.comparing(VoiceChannel::getName))
                .collect(Collectors.toList());

        if (matches.isEmpty() && query.startsWith("#")) {
            return findSorted(event, query.substring(1));
        } else {
            return matches;
        }
    }

    @Override
    protected @NonNull String getFieldNameForPicker(VoiceChannel item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(VoiceChannel item) {
        return item.getId();
    }

}
