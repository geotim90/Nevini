package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsInvasion {

    @SerializedName("node")
    String node;

    @SerializedName("desc")
    String desc;

    @SerializedName("attackingFaction")
    String attackingFaction;

    @SerializedName("attackerReward")
    WfsInvasionReward attackerReward;

    @SerializedName("defendingFaction")
    String defendingFaction;

    @SerializedName("defenderReward")
    WfsInvasionReward defenderReward;

}
