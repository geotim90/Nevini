package de.nevini.modules.osu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OsuBeatmapDifficultyId implements Serializable {

    private Integer beatmapId;

    private Integer mode;

    private Integer mods;

}
