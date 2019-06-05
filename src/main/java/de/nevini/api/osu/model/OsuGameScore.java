package de.nevini.api.osu.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OsuGameScore {

    private final Integer slot;
    private final OsuTeam team;
    private final Integer userId;
    private final Integer score;
    private final Integer maxCombo;
    private final OsuRank rank;
    private final Integer count50;
    private final Integer count100;
    private final Integer count300;
    private final Integer countMiss;
    private final Integer countKatu;
    private final Integer countGeki;
    private final Boolean perfect;
    private final Boolean pass;

}