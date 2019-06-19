package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsRotationReward {

    @SerializedName("_id")
    private final String id;

    @SerializedName("rotation")
    private final String rotation;

    @SerializedName("itemName")
    private final String itemName;

    @SerializedName("rarity")
    private final String rarity;

    @SerializedName("chance")
    private final Float chance;

}
