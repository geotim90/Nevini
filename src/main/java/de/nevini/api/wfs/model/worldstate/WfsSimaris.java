package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsSimaris {

    @SerializedName("asString")
    String asString;

}
