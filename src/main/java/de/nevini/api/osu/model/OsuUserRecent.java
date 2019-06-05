package de.nevini.api.osu.model;

import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuUserRecent {

    private final Integer beatmapId;
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
    private final Date date;
    private final OsuRank rank;

}
