package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsObjective {

    @SerializedName("_id")
    private final String id;

    @SerializedName("objectiveName")
    private final String objectiveName;

    @SerializedName("rewards")
    private final List<WfsRotationReward> rewards;

}
