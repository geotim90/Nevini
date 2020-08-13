package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class OsuApiMatchGameScore {

    @SerializedName("slot")
    Integer slot;

    @SerializedName("team")
    Integer team;

    @SerializedName("user_id")
    Integer userId;

    @SerializedName("score")
    Integer score;

    @SerializedName("maxcombo")
    Integer maxCombo;

    @SerializedName("rank")
    String rank;

    @SerializedName("count50")
    Integer count50;

    @SerializedName("count100")
    Integer count100;

    @SerializedName("count300")
    Integer count300;

    @SerializedName("countmiss")
    Integer countMiss;

    @SerializedName("countkatu")
    Integer countKatu;

    @SerializedName("countgeki")
    Integer countGeki;

    @SerializedName("perfect")
    Boolean perfect;

    @SerializedName("pass")
    Boolean pass;

    @SerializedName("enabled_mods")
    Integer mods;

}
