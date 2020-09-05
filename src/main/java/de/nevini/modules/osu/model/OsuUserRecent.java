package de.nevini.modules.osu.model;

import de.nevini.modules.osu.api.requests.OsuUserType;
import lombok.Value;

@Value
public class OsuUserRecent {

    String user;
    OsuUserType userType;
    OsuMode mode;
    Integer beatmapId;
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

}
