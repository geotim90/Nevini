package de.nevini.bot.resolvers.osu;

import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.AbstractResolver;
import de.nevini.bot.services.osu.OsuService;
import de.nevini.framework.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuBeatmapResolver extends AbstractResolver<OsuBeatmap> {

    OsuBeatmapResolver() {
        super("beatmap", new Pattern[]{Pattern.compile("(?i)(?:--|//)(?:beatmap|bm)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--beatmap] <beatmap>" : "--beatmap <beatmap>")
                .description("Refers to " + (list ? "all osu! beatmaps" : "an osu! beatmap")
                        + " with a matching id or name."
                        + (argument
                        ? "\nThe `--beatmap` flag is optional if this option is provided first."
                        : ""))
                .keyword("--beatmap")
                .aliases(new String[]{"//beatmap", "--bm", "//bm"})
                .build();
    }

    @Override
    public List<OsuBeatmap> findSorted(@NonNull CommandEvent event, String query) {
        return event.locate(OsuService.class).findBeatmaps(query).stream()
                .sorted(Comparator.comparing(OsuBeatmap::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    protected String getFieldNameForPicker(OsuBeatmap item) {
        return Integer.toString(item.getBeatmapId());
    }

    @Override
    protected String getFieldValueForPicker(OsuBeatmap item) {
        return "[" + item.getTitle() + " [" + item.getMode().getName() + "]" + "](https://osu.ppy.sh/b/"
                + item.getBeatmapId() + ")";
    }

}
