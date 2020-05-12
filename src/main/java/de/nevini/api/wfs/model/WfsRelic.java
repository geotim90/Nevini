package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsRelic {

    @SerializedName("_id")
    String id;

    @SerializedName("tier")
    String tier;

    @SerializedName("relicName")
    String relicName;

    @SerializedName("state")
    String state;

    @SerializedName("rewards")
    List<WfsReward> rewards;

}
