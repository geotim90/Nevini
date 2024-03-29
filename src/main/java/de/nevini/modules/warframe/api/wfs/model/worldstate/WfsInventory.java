package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsInventory {

    @SerializedName("item")
    String item;

    @SerializedName("ducats")
    Integer ducats;

    @SerializedName("credits")
    Integer credits;

}
