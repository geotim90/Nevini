package de.nevini.api.wfs.model;

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
