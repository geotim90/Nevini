package de.nevini.modules.osu.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiUserRecent {

    @SerializedName("beatmap_id")
    Integer beatmapId;

    @SerializedName("score")
    Integer score;

    @SerializedName("maxcombo")
    Integer maxCombo;

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

    @SerializedName("enabled_mods")
    Integer mods;

    @SerializedName("user_id")
    Integer userId;

    @SerializedName("date")
    Date date;

    @SerializedName("rank")
    String rank;

}
