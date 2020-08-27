package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsConstructionProgress {

    @SerializedName("fomorianProgress")
    String fomorianProgress;

    @SerializedName("razorbackProgress")
    String razorbackProgress;

}
