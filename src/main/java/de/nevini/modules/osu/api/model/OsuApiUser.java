package de.nevini.modules.osu.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class OsuApiUser {

    @SerializedName("user_id")
    Integer userId;

    @SerializedName("username")
    String userName;

    @SerializedName("join_date")
    Date joinDate;

    @SerializedName("count300")
    Integer count300;

    @SerializedName("count100")
    Integer count100;

    @SerializedName("count50")
    Integer count50;

    @SerializedName("playcount")
    Integer playCount;

    @SerializedName("ranked_score")
    Long rankedScore;

    @SerializedName("total_score")
    Long totalScore;

    @SerializedName("pp_rank")
    Integer ppRank;

    @SerializedName("level")
    Double level;

    @SerializedName("pp_raw")
    Double ppRaw;

    @SerializedName("accuracy")
    Double accuracy;

    @SerializedName("count_rank_ss")
    Integer countRankSs;

    @SerializedName("count_rank_ssh")
    Integer countRankSsh;

    @SerializedName("count_rank_s")
    Integer countRankS;

    @SerializedName("count_rank_sh")
    Integer countRankSh;

    @SerializedName("count_rank_a")
    Integer countRankA;

    @SerializedName("country")
    String country;

    @SerializedName("total_seconds_played")
    Integer totalSecondsPlayed;

    @SerializedName("pp_country_rank")
    Integer ppCountryRank;

    @SerializedName("events")
    List<OsuApiUserEvent> events;

}
