package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsJob {

    @SerializedName("rewardPool")
    List<String> rewardPool;

    @SerializedName("type")
    String type;

    @SerializedName("enemyLevels")
    List<Integer> enemyLevels;

    @SerializedName("standingStages")
    List<Integer> standingStages;

}
