package de.nevini.modules.osu.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiMatchMatch {

    @SerializedName("match_id")
    Integer matchId;

    @SerializedName("name")
    String name;

    @SerializedName("start_time")
    Date startTime;

    @SerializedName("end_time")
    Date endTime;

}
