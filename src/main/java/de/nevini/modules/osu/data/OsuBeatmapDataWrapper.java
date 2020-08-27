package de.nevini.modules.osu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OsuBeatmapDataWrapper {

    private OsuBeatmapData beatmap;

    private OsuBeatmapsetData beatmapset;

    private OsuBeatmapDifficultyData difficulty;

}
