package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.scope.Feed;
import de.nevini.util.Finder;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FeedResolver extends AbstractResolver<Feed> {

    FeedResolver() {
        super("feed", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)feed|[-/]f)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--feed] <type>" : "--feed <type>")
                .description("Refers to " + (list ? "all feed types" : "a specific feed type")
                        + " with a matching name."
                        + (argument ? "\nThe `--feed` flag is optional if this option is provided first." : ""))
                .keyword("--feed")
                .aliases(new String[]{"//feed", "-f", "/f"})
                .build();
    }

    @Override
    public List<Feed> findSorted(CommandEvent event, String query) {
        return Finder.findAny(Arrays.stream(Feed.values())
                        .filter(feed -> !feed.isOwnerOnly() || event.isBotOwner()).collect(Collectors.toList()),
                feed -> new String[]{
                        feed.getType(), feed.getName(), feed.name(),
                        feed.name().replace('_', ' ')
                }, query
        );
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
