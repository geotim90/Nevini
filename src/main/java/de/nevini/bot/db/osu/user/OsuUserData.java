package de.nevini.bot.db.osu.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(OsuUserId.class)
@Table(name = "osu_user")
public class OsuUserData {

    @Id
    private Integer userId;

    @Id
    private Integer mode;

    private String userName;

    private Long joinDate;

    private Integer count300;

    private Integer count100;

    private Integer count50;

    private Integer playCount;

    private Long rankedScore;

    private Long totalScore;

    private Integer ppRank;

    private Double level;

    private Double ppRaw;

    private Double accuracy;

    private Integer countRankSs;

    private Integer countRankSsh;

    @Column(name = "count_rank_s")
    private Integer countRankS;

    private Integer countRankSh;

    @Column(name = "count_rank_a")
    private Integer countRankA;

    private String country;

    private Integer totalSecondsPlayed;

    private Integer ppCountryRank;

}
