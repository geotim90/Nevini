package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsCambionCycle {

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("active")
    String active;

}
