package de.nevini.modules.osu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(OsuBeatmapDifficultyId.class)
@Table(name = "osu_beatmap_difficulty")
public class OsuBeatmapDifficultyData {

    @Id
    private Integer beatmapId;

    @Id
    private Integer mode;

    @Id
    private Integer mods;

    /**
     * Star Difficulty
     */
    private Double difficultyRating;

    private Double difficultyAim;

    private Double difficultySpeed;

    private Integer maxCombo;

    private Double maxPp;

}
