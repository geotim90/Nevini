package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.scope.Feed;
import de.nevini.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class FeedResolver extends AbstractResolver<Feed> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--feed <type>")
                .description("Refers to a specific feed type.")
                .keyword("--feed")
                .aliases(new String[]{"//feed", "-f", "/f"});
    }

    protected FeedResolver() {
        super("feed", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)feed|[-/]f)(?:\\s+(.+))?")});
    }

    @Override
    public List<Feed> findSorted(CommandEvent ignored, String query) {
        return Finder.findAny(Feed.values(), feed -> new String[]{feed.getType(), feed.getName(), feed.name(), feed.name().replace('_', ' ')}, query);
    }

    @Override
    protected String getFieldNameForPicker(Feed item) {
        return item.getType();
    }

    @Override
    protected String getFieldValueForPicker(Feed item) {
        return item.getName();
    }

}
