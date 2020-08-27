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
@IdClass(OsuScoreId.class)
@Table(name = "osu_score")
public class OsuScoreData {

    private Long scoreId;

    @Id
    private Integer beatmapId;

    @Id
    private Integer mode;

    @Id
    private Integer mods;

    private Integer userId;

    private String userName;

    private Integer score;

    private Integer count300;

    private Integer count100;

    private Integer count50;

    private Integer countMiss;

    private Integer maxCombo;

    private Integer countKatu;

    private Integer countGeki;

    private Boolean perfect;

    private Long date;

    private String rank;

    private Double pp;

    private Boolean replayAvailable;

}
