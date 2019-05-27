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

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--beatmap <beatmap>")
                .description("Refers to an osu! beatmap.")
                .keyword("--beatmap")
                .aliases(new String[]{"//beatmap", "--bm", "//bm"});
    }

    private final OsuService osuService;

    public OsuBeatmapResolver(@NonNull OsuService osuService) {
        super("beatmap", new Pattern[]{Pattern.compile("(?i)(?:--|//)(?:beatmap|bm)(?:\\s+(.+))?")});
        this.osuService = osuService;
    }


    @Override
    public List<OsuBeatmap> findSorted(CommandEvent ignored, String query) {
        return osuService.findBeatmaps(query).stream().sorted(Comparator.comparing(OsuBeatmap::getTitle))
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
