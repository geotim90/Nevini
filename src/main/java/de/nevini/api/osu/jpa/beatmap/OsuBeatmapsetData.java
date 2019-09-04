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
@Table(name = "osu_beatmapset")
public class OsuBeatmapsetData {

    @Id
    private Integer beatmapsetId;

    private Integer approved;

    private Long submitDate;

    private Long approvedDate;

    private Long lastUpdate;

    private String artist;

    private String creatorName;

    private Integer creatorId;

    private String source;

    private Integer genre;

    private Integer language;

    private String title;

    private String tags;

    private Boolean downloadUnavailable;

    private Boolean audioUnavailable;

}
