package de.nevini.api.osu.model;

import com.oopsjpeg.osu4j.GameMod;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuScore {

    private final Integer scoreId;
    private final Integer score;
    private final String userName;
    private final Integer count300;
    private final Integer count100;
    private final Integer count50;
    private final Integer countMiss;
    private final Integer maxCombo;
    private final Integer countKatu;
    private final Integer countGeki;
    private final Boolean perfect;
    private final GameMod[] mods;
    private final Integer userId;
    private final Date date;
    private final OsuRank rank;
    private final Float pp;
    private final Boolean replayAvailable;

}
