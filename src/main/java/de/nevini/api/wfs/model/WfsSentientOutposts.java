package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsSentientOutposts {

    @SerializedName("mission")
    WfsSentientOutpostsMission mission;

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("expiry")
    OffsetDateTime expiry;

}
