package de.nevini.api.osu.model;

import lombok.Value;

@Value
public class OsuUser {

    Integer userId;
    OsuMode mode;
    String userName;
    Long joinDate;
    Integer count300;
    Integer count100;
    Integer count50;
    Integer playCount;
    Long rankedScore;
    Long totalScore;
    Integer ppRank;
    Double level;
    Double ppRaw;
    Double accuracy;
    Integer countRankSs;
    Integer countRankSsh;
    Integer countRankS;
    Integer countRankSh;
    Integer countRankA;
    String country;
    Integer totalSecondsPlayed;
    Integer ppCountryRank;

}
