package de.nevini.modules.warframe.api.wfs.model.worldstate;

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
