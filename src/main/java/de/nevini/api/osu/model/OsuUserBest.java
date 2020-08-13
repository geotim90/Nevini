package de.nevini.api.osu.model;

import de.nevini.api.osu.external.requests.OsuUserType;
import lombok.Value;

@Value
public class OsuUserBest {

    String user;
    OsuUserType userType;
    OsuMode mode;
    Integer beatmapId;
    Long scoreId;
    Integer score;
    Integer maxCombo;
    Integer count50;
    Integer count100;
    Integer count300;
    Integer countMiss;
    Integer countKatu;
    Integer countGeki;
    Boolean perfect;
    OsuMod[] mods;
    Integer userId;
    Long date;
    OsuRank rank;
    Double pp;

}
