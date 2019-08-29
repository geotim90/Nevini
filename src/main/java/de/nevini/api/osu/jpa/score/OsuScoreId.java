package de.nevini.api.osu.jpa.score;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OsuScoreId implements Serializable {

    private Integer beatmapId;

    private Integer mode;

    private Integer mods;

}
