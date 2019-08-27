package de.nevini.api.osu.model;

import de.nevini.api.osu.external.requests.OsuUserType;
import lombok.Value;

@Value
public class OsuUserBest {

    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final Integer beatmapId;
    private final Long scoreId;
    private final Integer score;
    private final Integer maxCombo;
    private final Integer count50;
    private final Integer count100;
    private final Integer count300;
    private final Integer countMiss;
    private final Integer countKatu;
    private final Integer countGeki;
    private final Boolean perfect;
    private final OsuMod[] mods;
    private final Integer userId;
    private final Long date;
    private final OsuRank rank;
    private final Double pp;

}
