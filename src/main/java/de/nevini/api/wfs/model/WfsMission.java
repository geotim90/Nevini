package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsMission {

    @SerializedName("gameMode")
    String gameMode;

    @SerializedName("isEvent")
    Boolean isEvent;

    @SerializedName("rewards")
    WfsRewards rewards;

}
