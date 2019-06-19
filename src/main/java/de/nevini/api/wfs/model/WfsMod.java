package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsMod {

    @SerializedName("_id")
    private final String id;

    @SerializedName("modName")
    private final String modName;

    @SerializedName("rarity")
    private final String rarity;

    @SerializedName("chance")
    private final Float chance;

}
