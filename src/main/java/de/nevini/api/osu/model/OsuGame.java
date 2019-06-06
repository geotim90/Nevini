package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuGame {

    @SerializedName("game_id")
    private final Integer gameId;

    @SerializedName("start_time")
    private final Date startTime;

    @SerializedName("end_time")
    private final Date endTime;

    @SerializedName("beatmap_id")
    private final Integer beatmapId;

    @SerializedName("play_mode")
    private final OsuMode playMode;

    @SerializedName("match_type")
    private final OsuMatchType matchType;

    @SerializedName("scoring_type")
    private final OsuScoringType scoringType;

    @SerializedName("team_type")
    private final OsuTeamType teamType;

    @SerializedName("mods")
    private final OsuMod[] mods;

    @SerializedName("scores")
    private final List<OsuGameScore> scores;

}
