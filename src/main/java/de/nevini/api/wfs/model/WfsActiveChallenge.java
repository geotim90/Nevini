package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsActiveChallenge {

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("isDaily")
    Boolean isDaily;

    @SerializedName("isElite")
    Boolean isElite;

    @SerializedName("desc")
    String desc;

    @SerializedName("title")
    String title;

    @SerializedName("reputation")
    Integer reputation;

}
