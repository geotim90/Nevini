package de.nevini.bot.resolvers.common;

import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.resolvers.AbstractResolver;
import de.nevini.bot.scope.Feed;
import de.nevini.bot.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class FeedResolver extends AbstractResolver<Feed> {

    protected FeedResolver() {
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
    public List<Feed> findSorted(CommandEvent ignored, String query) {
        return Finder.findAny(Feed.values(), feed -> new String[]{feed.getType(), feed.getName(), feed.name(),
                feed.name().replace('_', ' ')}, query);
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
