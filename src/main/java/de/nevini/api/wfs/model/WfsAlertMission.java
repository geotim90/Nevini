package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsAlertMission {

    @SerializedName("description")
    String description;

    @SerializedName("node")
    String node;

    @SerializedName("type")
    String type;

    @SerializedName("faction")
    String faction;

    @SerializedName("reward")
    WfsAlertMissionReward reward;

    @SerializedName("minEnemyLevel")
    Integer minEnemyLevel;

    @SerializedName("maxEnemyLevel")
    Integer maxEnemyLevel;

}
