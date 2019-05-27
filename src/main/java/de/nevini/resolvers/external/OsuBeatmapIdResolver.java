package de.nevini.resolvers.external;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.services.external.OsuService;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuBeatmapIdResolver extends AbstractResolver<Integer> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--beatmap <beatmap>")
                .description("Refers to an osu! beatmap.")
                .keyword("--beatmap")
                .aliases(new String[]{"//beatmap", "--bm", "//bm"});
    }

    private final OsuService osuService;

    public OsuBeatmapIdResolver(@NonNull OsuService osuService) {
        super("beatmap", new Pattern[]{Pattern.compile("(?i)(?:--|//)(?:beatmap|bm)(?:\\s+(.+))?")});
        this.osuService = osuService;
    }


    @Override
    public List<Integer> findSorted(CommandEvent ignored, String query) {
        return osuService.findBeatmaps(query).entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    protected String getFieldNameForPicker(Integer item) {
        return Integer.toString(item);
    }

    @Override
    protected String getFieldValueForPicker(Integer item) {
        return osuService.getBeatmapName(item);
    }

}
