package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsFissure {

    @SerializedName("node")
    String node;

    @SerializedName("missionType")
    String missionType;

    @SerializedName("enemy")
    String enemy;

    @SerializedName("tier")
    String tier;

    @SerializedName("tierNum")
    Integer tierNum;

    @SerializedName("expiry")
    OffsetDateTime expiry;

}
