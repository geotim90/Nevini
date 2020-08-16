package de.nevini.api.wfs.model.drops;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class WfsBounty {

    @SerializedName("_id")
    String id;

    @SerializedName("bountyLevel")
    String bountyLevel;

    @SerializedName("rewards")
    Map<String, List<WfsStageReward>> rewards;

}
