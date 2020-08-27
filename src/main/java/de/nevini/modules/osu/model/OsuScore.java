package de.nevini.modules.osu.model;

import lombok.Value;

@Value
public class OsuScore {

    Long scoreId;
    Integer beatmapId;
    OsuMode mode;
    OsuMod[] mods;
    Integer userId;
    String userName;
    Integer score;
    Integer count300;
    Integer count100;
    Integer count50;
    Integer countMiss;
    Integer maxCombo;
    Integer countKatu;
    Integer countGeki;
    Boolean perfect;
    Long date;
    OsuRank rank;
    Double pp;
    Boolean replayAvailable;

}
