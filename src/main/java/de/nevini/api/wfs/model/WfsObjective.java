package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsObjective {

    @SerializedName("_id")
    String id;

    @SerializedName("objectiveName")
    String objectiveName;

    @SerializedName("rewards")
    List<WfsRotationReward> rewards;

}
