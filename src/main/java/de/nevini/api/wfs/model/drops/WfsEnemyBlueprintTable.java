package de.nevini.api.wfs.model.drops;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsEnemyBlueprintTable {

    @SerializedName("_id")
    String id;

    @SerializedName("enemyName")
    String enemyName;

    @SerializedName("enemyItemDropChance")
    Float enemyItemDropChance;

    @SerializedName("blueprintDropChance")
    Float blueprintDropChance;

    @SerializedName("items")
    List<WfsItem> items;

    @SerializedName("mods")
    List<WfsMod> mods;

}
