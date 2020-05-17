package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsArbitration {

    @SerializedName("node")
    String node;

    @SerializedName("type")
    String type;

    @SerializedName("enemy")
    String enemy;

    @SerializedName("expiry")
    OffsetDateTime expiry;

}
