package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsEnemyModTable {

    @SerializedName("_id")
    private final String id;

    @SerializedName("enemyName")
    private final String enemyName;

    @SerializedName("ememyModDropChance")
    private final Float enemyModDropChance;

    @SerializedName("mods")
    private final List<WfsMod> mods;

}
