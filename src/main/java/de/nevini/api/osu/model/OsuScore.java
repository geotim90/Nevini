package de.nevini.api.osu.model;

import lombok.Value;

@Value
public class OsuScore {

    private final Long scoreId;
    private final Integer beatmapId;
    private final OsuMode mode;
    private final OsuMod[] mods;
    private final Integer userId;
    private final String userName;
    private final Integer score;
    private final Integer count300;
    private final Integer count100;
    private final Integer count50;
    private final Integer countMiss;
    private final Integer maxCombo;
    private final Integer countKatu;
    private final Integer countGeki;
    private final Boolean perfect;
    private final Long date;
    private final OsuRank rank;
    private final Double pp;
    private final Boolean replayAvailable;

}
