package de.nevini.modules.osu.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class OsuApiMatchGame {

    @SerializedName("game_id")
    Integer gameId;

    @SerializedName("start_time")
    Date startTime;

    @SerializedName("end_time")
    Date endTime;

    @SerializedName("beatmap_id")
    Integer beatmapId;

    @SerializedName("play_mode")
    Integer mode;

    @SerializedName("match_type")
    Integer matchType;

    @SerializedName("scoring_type")
    Integer scoringType;

    @SerializedName("team_type")
    Integer teamType;

    @SerializedName("mods")
    Integer mods;

    @SerializedName("scores")
    List<OsuApiMatchGameScore> scores;

}
