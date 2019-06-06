package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OsuGameScore {

    @SerializedName("slot")
    private final Integer slot;

    @SerializedName("team")
    private final OsuTeam team;

    @SerializedName("user_id")
    private final Integer userId;

    @SerializedName("score")
    private final Integer score;

    @SerializedName("maxcombo")
    private final Integer maxCombo;

    @SerializedName("rank")
    private final OsuRank rank;

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

    @SerializedName("pass")
    private final Boolean pass;

}
