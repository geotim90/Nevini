package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuMatchData {

    @SerializedName("match_id")
    private final Integer matchId;

    @SerializedName("name")
    private final String name;

    @SerializedName("start_time")
    private final Date startTime;

    @SerializedName("end_time")
    private final Date endTime;

}
