package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Value
public class WfsVoidTrader {

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("active")
    Boolean active;

    @SerializedName("location")
    String location;

    @SerializedName("inventory")
    List<WfsInventory> inventory;

    @SerializedName("expiry")
    OffsetDateTime expiry;

}
