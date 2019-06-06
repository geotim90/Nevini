package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuUserBest {

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
    private final OsuMod[] mods;

    @SerializedName("user_id")
    private final Integer userId;

    @SerializedName("date")
    private final Date date;

    @SerializedName("rank")
    private final OsuRank rank;

    @SerializedName("pp")
    private final Float pp;

}
