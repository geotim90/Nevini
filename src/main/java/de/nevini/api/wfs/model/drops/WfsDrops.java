package de.nevini.api.wfs.model.drops;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class WfsDrops {

    @SerializedName("missionRewards")
    Map<String, Map<String, WfsMission>> missionRewards;

    @SerializedName("relics")
    List<WfsRelic> relics;

    @SerializedName("transientRewards")
    List<WfsObjective> transientRewards;

    @SerializedName("modLocations")
    List<WfsModLocation> modLocations;

    @SerializedName("enemyModTables")
    List<WfsEnemyModTable> enemyModTables;

    @SerializedName("blueprintLocations")
    List<WfsBlueprintLocation> blueprintLocations;

    @SerializedName("enemyBlueprintTables")
    List<WfsEnemyBlueprintTable> enemyBlueprintTables;

    @SerializedName("sortieRewards")
    List<WfsItem> sortieRewards;

    @SerializedName("keyRewards")
    List<WfsKey> keyRewards;

    @SerializedName("cetusBountyRewards")
    List<WfsBounty> cetusBountyRewards;

    @SerializedName("solarisBountyRewards")
    List<WfsBounty> solarisBountyRewards;

}
