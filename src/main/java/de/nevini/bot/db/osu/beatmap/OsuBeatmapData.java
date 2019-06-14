package de.nevini.bot.db.osu.beatmap;

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

    private Double difficultyRating;

    private Double difficultyAim;

    private Double difficultySpeed;

    private Double difficultySize;

    private Double difficultyOverall;

    private Double difficultyApproach;

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

    private String tags;

    private Integer favouriteCount;

    private Double rating;

    private Integer playCount;

    private Integer passCount;

    private Integer countNormal;

    private Integer countSlider;

    private Integer countSpinner;

    private Integer maxCombo;

    private Boolean downloadUnavailable;

    private Boolean audioUnavailable;

}
