package de.nevini.resolvers;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChannelResolver extends AbstractResolver<TextChannel> {

    public ChannelResolver() {
        super("channel", new Pattern[]{
                Pattern.compile("<@#(\\d+)>"),
                Pattern.compile("(?i)(?:(?:--|//)channel|[-/]c)(?:\\s+(.+))")
        });
    }

    @Override
    protected List<TextChannel> findSorted(@NonNull CommandEvent event, String query) {
        return FinderUtil.findTextChannels(query, event.getGuild()).stream()
                .sorted(Comparator.comparing(TextChannel::getName))
                .collect(Collectors.toList());
    }

    @Override
    protected String getFieldNameForPicker(TextChannel item) {
        return item.getName();
    }

    @Override
    protected String getFieldValueForPicker(TextChannel item) {
        return item.getId();
    }

}
