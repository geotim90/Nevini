package de.nevini.modules.osu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "osu_beatmap")
public class OsuBeatmapData {

    @Id
    private Integer beatmapId;

    private Integer beatmapsetId;

    private Double bpm;

    /**
     * Circle Size (CS)
     */
    private Double difficultySize;

    /**
     * Overall Difficulty (OD)
     */
    private Double difficultyOverall;

    /**
     * Approach Rate (AR)
     */
    private Double difficultyApproach;

    /**
     * HP Drain Rate (HP)
     */
    private Double difficultyDrain;

    private Integer hitLength;

    private Integer totalLength;

    private String version;

    private String fileMd5;

    private Integer mode;

    private Integer favouriteCount;

    private Double rating;

    private Integer playCount;

    private Integer passCount;

    private Integer countNormal;

    private Integer countSlider;

    private Integer countSpinner;

}
