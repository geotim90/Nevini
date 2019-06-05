package de.nevini.api.osu.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuUser {

    private final Integer userId;
    private final String userName;
    private final Date joinDate;
    private final Integer count300;
    private final Integer count100;
    private final Integer count50;
    private final Integer playCount;
    private final Long rankedScore;
    private final Long totalScore;
    private final Integer ppRank;
    private final Float level;
    private final Float ppRaw;
    private final Float accuracy;
    private final Integer countRankSs;
    private final Integer countRankSsh;
    private final Integer countRankS;
    private final Integer countRankA;
    private final String country;
    private final Integer totalSecondsPlayed;
    private final Integer ppCountryRank;
    private final List<OsuUserEvent> events;

}
