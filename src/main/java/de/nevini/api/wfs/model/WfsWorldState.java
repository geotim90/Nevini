package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsWorldState {

    @SerializedName("voidTrader")
    WfsVoidTrader voidTrader;

}
