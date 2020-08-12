package de.nevini.api.wfs.model.drops;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsMod {

    @SerializedName("_id")
    String id;

    @SerializedName("modName")
    String modName;

    @SerializedName("rarity")
    String rarity;

    @SerializedName("chance")
    Float chance;

}
