package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class WfsDrops {

    @SerializedName("missionRewards")
    private final Map<String, Map<String, WfsMission>> missionRewards;

    @SerializedName("relics")
    private final List<WfsRelic> relics;

    @SerializedName("transientRewards")
    private final List<WfsObjective> transientRewards;

    @SerializedName("modLocations")
    private final List<WfsModLocation> modLocations;

    @SerializedName("enemyModTables")
    private final List<WfsEnemyModTable> enemyModTables;

    @SerializedName("blueprintLocations")
    private final List<WfsBlueprintLocation> blueprintLocations;

    @SerializedName("enemyBlueprintTables")
    private final List<WfsEnemyBlueprintTable> enemyBlueprintTables;

    @SerializedName("sortieRewards")
    private final List<WfsItem> sortieRewards;

    @SerializedName("keyRewards")
    private final List<WfsKey> keyRewards;

    @SerializedName("cetusBountyRewards")
    private final List<WfsBounty> cetusBountyRewards;

    @SerializedName("solarisBountyRewards")
    private final List<WfsBounty> solarisBountyRewards;

}
