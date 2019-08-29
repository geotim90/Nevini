package de.nevini.api.osu.model;

import lombok.Value;

@Value
public class OsuUser {

    private final Integer userId;
    private final OsuMode mode;
    private final String userName;
    private final Long joinDate;
    private final Integer count300;
    private final Integer count100;
    private final Integer count50;
    private final Integer playCount;
    private final Long rankedScore;
    private final Long totalScore;
    private final Integer ppRank;
    private final Double level;
    private final Double ppRaw;
    private final Double accuracy;
    private final Integer countRankSs;
    private final Integer countRankSsh;
    private final Integer countRankS;
    private final Integer countRankSh;
    private final Integer countRankA;
    private final String country;
    private final Integer totalSecondsPlayed;
    private final Integer ppCountryRank;

}
