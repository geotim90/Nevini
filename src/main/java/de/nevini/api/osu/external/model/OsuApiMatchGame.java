package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
public class OsuApiMatchGame {

    @SerializedName("game_id")
    private final Integer gameId;

    @SerializedName("start_time")
    private final Date startTime;

    @SerializedName("end_time")
    private final Date endTime;

    @SerializedName("beatmap_id")
    private final Integer beatmapId;

    @SerializedName("play_mode")
    private final Integer mode;

    @SerializedName("match_type")
    private final Integer matchType;

    @SerializedName("scoring_type")
    private final Integer scoringType;

    @SerializedName("team_type")
    private final Integer teamType;

    @SerializedName("mods")
    private final Integer mods;

    @SerializedName("scores")
    private final List<OsuApiMatchGameScore> scores;

}
