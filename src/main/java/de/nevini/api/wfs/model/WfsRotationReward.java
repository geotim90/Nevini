package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsRotationReward {

    @SerializedName("_id")
    String id;

    @SerializedName("rotation")
    String rotation;

    @SerializedName("itemName")
    String itemName;

    @SerializedName("rarity")
    String rarity;

    @SerializedName("chance")
    Float chance;

}
