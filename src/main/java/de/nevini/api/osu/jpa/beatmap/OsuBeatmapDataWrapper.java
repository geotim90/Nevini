package de.nevini.api.osu.jpa.beatmap;

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
