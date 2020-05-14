package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsVoidTrader {

    @SerializedName("startString")
    String startString;

    @SerializedName("active")
    Boolean active;

    @SerializedName("location")
    String location;

    @SerializedName("inventory")
    List<WfsInventory> inventory;

    @SerializedName("endString")
    String endString;

}
