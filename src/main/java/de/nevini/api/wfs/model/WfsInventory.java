package de.nevini.api.wfs.model;

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
