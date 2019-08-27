package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class OsuApiUser {

    @SerializedName("user_id")
    private final Integer userId;

    @SerializedName("username")
    private final String userName;

    @SerializedName("join_date")
    private final Date joinDate;

    @SerializedName("count300")
    private final Integer count300;

    @SerializedName("count100")
    private final Integer count100;

    @SerializedName("count50")
    private final Integer count50;

    @SerializedName("playcount")
    private final Integer playCount;

    @SerializedName("ranked_score")
    private final Long rankedScore;

    @SerializedName("total_score")
    private final Long totalScore;

    @SerializedName("pp_rank")
    private final Integer ppRank;

    @SerializedName("level")
    private final Double level;

    @SerializedName("pp_raw")
    private final Double ppRaw;

    @SerializedName("accuracy")
    private final Double accuracy;

    @SerializedName("count_rank_ss")
    private final Integer countRankSs;

    @SerializedName("count_rank_ssh")
    private final Integer countRankSsh;

    @SerializedName("count_rank_s")
    private final Integer countRankS;

    @SerializedName("count_rank_sh")
    private final Integer countRankSh;

    @SerializedName("count_rank_a")
    private final Integer countRankA;

    @SerializedName("country")
    private final String country;

    @SerializedName("total_seconds_played")
    private final Integer totalSecondsPlayed;

    @SerializedName("pp_country_rank")
    private final Integer ppCountryRank;

    @SerializedName("events")
    private final List<OsuApiUserEvent> events;

}
