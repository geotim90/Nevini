package de.nevini.api.wfs.model.drops;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WfsDrop {

    @SerializedName("item")
    String item;

    @SerializedName("place")
    String place;

    @SerializedName("rarity")
    String rarity;

    @SerializedName("chance")
    Float chance;

}
