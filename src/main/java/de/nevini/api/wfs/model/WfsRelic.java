package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsRelic {

    @SerializedName("_id")
    private final String id;

    @SerializedName("tier")
    private final String tier;

    @SerializedName("relicName")
    private final String relicName;

    @SerializedName("state")
    private final String state;

    @SerializedName("rewards")
    private final List<WfsReward> rewards;

}
