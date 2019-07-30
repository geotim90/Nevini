package de.nevini.resolvers.osu;

import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.services.osu.OsuService;
import de.nevini.util.command.CommandOptionDescriptor;
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
                .sorted(Comparator.comparing(OsuBeatmap::getTitle).thenComparing(OsuBeatmap::getDifficultyRating))
                .collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(OsuBeatmap item) {
        return Integer.toString(item.getBeatmapId());
    }

    @Override
    protected @NonNull String getFieldValueForPicker(OsuBeatmap item) {
        return "[" + item.getArtist() + " - " + item.getTitle() + " [" + item.getVersion() + "] ("
                + item.getMode().getName() + ")](https://osu.ppy.sh/b/" + item.getBeatmapId() + ")";
    }

}
