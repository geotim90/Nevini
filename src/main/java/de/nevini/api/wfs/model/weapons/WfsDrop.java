package de.nevini.api.wfs.model.weapons;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsDrop {

    @SerializedName("location")
    String location;

    @SerializedName("type")
    String type;

    @SerializedName("rarity")
    String rarity;

    @SerializedName("chance")
    Float chance;

}
