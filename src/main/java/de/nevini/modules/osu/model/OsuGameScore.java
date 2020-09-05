package de.nevini.modules.osu.model;

import lombok.Value;

@Value
public class OsuGameScore {

    Integer slot;
    OsuTeam team;
    Integer userId;
    Integer score;
    Integer maxCombo;
    String rank; // always "0"
    Integer count50;
    Integer count100;
    Integer count300;
    Integer countMiss;
    Integer countKatu;
    Integer countGeki;
    Boolean perfect;
    Boolean pass;
    OsuMod[] mods;

}
