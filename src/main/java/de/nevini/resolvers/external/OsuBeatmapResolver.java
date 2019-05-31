package de.nevini.resolvers.external;

import com.oopsjpeg.osu4j.OsuBeatmap;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.services.external.OsuService;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuBeatmapResolver extends AbstractResolver<OsuBeatmap> {

    protected OsuBeatmapResolver() {
        super("beatmap", new Pattern[]{Pattern.compile("(?i)(?:--|//)(?:beatmap|bm)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean resolvesArgument, boolean resolvesList) {
        return CommandOptionDescriptor.builder()
                .syntax(resolvesArgument ? "[--beatmap] <beatmap>" : "--beatmap <beatmap>")
                .description("Refers to " + (resolvesList ? "all osu! beatmaps" : "an osu! beatmap")
                        + " with a matching id or name."
                        + (resolvesArgument ? "\nThe `--beatmap` flag is optional if this option is provided first." : ""))
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
        return Integer.toString(item.getID());
    }

    @Override
    protected String getFieldValueForPicker(OsuBeatmap item) {
        return "[" + item.getTitle() + " [" + item.getMode().getName() + "]" + "](https://osu.ppy.sh/b/" + item.getID() + ")";
    }

}
