package de.nevini.api.osu.jpa.beatmap;

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
@Table(name = "osu_beatmap_view")
public class OsuBeatmapViewData {

    private Integer approved;

    private Long submitDate;

    private Long approvedDate;

    private Long lastUpdate;

    private String artist;

    @Id
    private Integer beatmapId;

    private Integer beatmapsetId;

    private Double bpm;

    private String creatorName;

    private Integer creatorId;

    /**
     * Star Difficulty
     */
    private Double difficultyRating;

    private Double difficultyAim;

    private Double difficultySpeed;

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

    private String source;

    private Integer genre;

    private Integer language;

    private String title;

    private Integer totalLength;

    private String version;

    private String fileMd5;

    private Integer mode;

    private Integer convertedMode;

    private Integer mods;

    private String tags;

    private Integer favouriteCount;

    private Double rating;

    private Integer playCount;

    private Integer passCount;

    private Integer countNormal;

    private Integer countSlider;

    private Integer countSpinner;

    private Integer maxCombo;

    private Double maxPp;

    private Boolean downloadUnavailable;

    private Boolean audioUnavailable;

}
