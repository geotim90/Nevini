package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiUserBest {

    @SerializedName("beatmap_id")
    private final Integer beatmapId;

    @SerializedName("score_id")
    private final Long scoreId;

    @SerializedName("score")
    private final Integer score;

    @SerializedName("maxcombo")
    private final Integer maxCombo;

    @SerializedName("count50")
    private final Integer count50;

    @SerializedName("count100")
    private final Integer count100;

    @SerializedName("count300")
    private final Integer count300;

    @SerializedName("countmiss")
    private final Integer countMiss;

    @SerializedName("countkatu")
    private final Integer countKatu;

    @SerializedName("countgeki")
    private final Integer countGeki;

    @SerializedName("perfect")
    private final Boolean perfect;

    @SerializedName("enabled_mods")
    private final Integer mods;

    @SerializedName("user_id")
    private final Integer userId;

    @SerializedName("date")
    private final Date date;

    @SerializedName("rank")
    private final String rank;

    @SerializedName("pp")
    private final Double pp;

}
