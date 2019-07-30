package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Guild;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuildResolver extends AbstractResolver<Guild> {

    GuildResolver() {
        super("server", new Pattern[]{
                Pattern.compile("(?i)(?:(?:--|//)(?:server|guild)|[-/][sg])(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax("[--server] [<server>]")
                .description("Refers to " + (list ? "all servers" : "a specific server")
                        + " with a matching id or name.\n"
                        + (argument ? "The `--server` flag is optional if this option is provided first" : "") + ".\n"
                        + "Refers to the current server if only the `--server` flag is provided.")
                .keyword("--server")
                .aliases(new String[]{"//server", "--guild", "//guild", "-s", "/s", "-g", "/g"})
                .build();
    }

    @Override
    public List<Guild> findSorted(@NonNull CommandEvent event, String query) {
        return Finder.findAny(
                event.getJDA().getGuilds(), e -> new String[]{e.getId(), e.getName()}, query
        ).stream().sorted(Comparator.comparing(Guild::getName)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(Guild item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(Guild item) {
        return item.getId();
    }

}
