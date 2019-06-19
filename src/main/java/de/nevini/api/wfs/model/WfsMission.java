package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsMission {

    @SerializedName("gameMode")
    private final String gameMode;

    @SerializedName("isEvent")
    private final Boolean isEvent;

    @SerializedName("rewards")
    private final WfsRewards rewards;

}
