package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsAlert {

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("mission")
    WfsAlertMission mission;

}
