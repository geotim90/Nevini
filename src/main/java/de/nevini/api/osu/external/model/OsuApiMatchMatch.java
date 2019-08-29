package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiMatchMatch {

    @SerializedName("match_id")
    private final Integer matchId;

    @SerializedName("name")
    private final String name;

    @SerializedName("start_time")
    private final Date startTime;

    @SerializedName("end_time")
    private final Date endTime;

}
