package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsEnemyModTable {

    @SerializedName("_id")
    String id;

    @SerializedName("enemyName")
    String enemyName;

    @SerializedName("enemyModDropChance")
    Float enemyModDropChance;

    @SerializedName("mods")
    List<WfsMod> mods;

}
