package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsEnemyBlueprintTable {

    @SerializedName("_id")
    private final String id;

    @SerializedName("enemyName")
    private final String enemyName;

    @SerializedName("enemyItemDropChance")
    private final Float enemyItemDropChance;

    @SerializedName("blueprintDropChance")
    private final Float blueprintDropChance;

    @SerializedName("items")
    private final List<WfsItem> items;

    @SerializedName("mods")
    private final List<WfsMod> mods;

}
