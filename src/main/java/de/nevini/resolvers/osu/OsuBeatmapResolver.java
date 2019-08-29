package de.nevini.resolvers.osu;

import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.services.osu.OsuService;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuBeatmapResolver extends OptionResolver<OsuBeatmap> {

    OsuBeatmapResolver() {
        super("beatmap", new Pattern[]{Pattern.compile("(?i)(?:--|//)(?:beatmap|bm)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--beatmap] <beatmap>" : "--beatmap <beatmap>")
                .description("Refers to " + (list ? "all osu! beatmaps" : "an osu! beatmap")
                        + " with a matching id or name. You can also provide search criteria similar to the in-game"
                        + " search function (<https://osu.ppy.sh/help/wiki/Interface#search>):"
                        + "\n* `ar` - Approach Rate (AR)"
                        + "\n* `cs` - Circle Size (CS)"
                        + "\n* `od` - Overall Difficulty (OD)"
                        + "\n* `hp` - HP Drain Rate (HP)"
                        + "\n* `stars` - Star Difficulty"
                        + "\n* `aim` - Aim Difficulty"
                        + "\n* `speed` - Speed Difficulty"
                        + "\n* `bpm` - Beats per minute"
                        + "\n* `length` - Length in seconds"
                        + "\n* `drain` - Drain Time in seconds"
                        + "\n* `combo` - Max combo"
                        + "\n* `pp` - Max performance (without mods)"
                        + "\n* `mode` - Mode - value can be `osu`, `taiko`, `catchthebeat`, or `mania`, or `o/t/c/m` for short"
                        + "\n* `status` - Ranked status - value can be `ranked`, `approved`, `pending`, `notsubmitted`, `unknown`, or `loved`, or `r/a/p/n/u/l` for short"
                        + "\n* `b` - Beatmap id"
                        + "\n* `s` - Beatmapset id"
                        + "\n* `title` - Song title"
                        + "\n* `artist` - Song artist"
                        + "\n* `diff` - Beatmap difficulty name / version"
                        + "\n* `mapper` - Mapper name"
                        + "\n* `source` - Song source"
                        + "\n* `genre` - Song genre"
                        + "\n* `language` - Song language"
                        + "\n* `tag` - Beatmapset tags"
                        + "\n* `rating` - Beatmapset rating"
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
