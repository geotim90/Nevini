package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class WfsBounty {

    @SerializedName("_id")
    private final String id;

    @SerializedName("bountyLevel")
    private final String bountyLevel;

    @SerializedName("rewards")
    private final Map<String, List<WfsStageReward>> rewards;

}
