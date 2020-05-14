package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsEnemy {

    @SerializedName("_id")
    String id;

    @SerializedName("enemyName")
    String enemyName;

    @SerializedName("enemyModDropChance")
    Float enemyModDropChance;

    @SerializedName("enemyItemDropChance")
    Float enemyItemDropChance;

    @SerializedName("enemyBlueprintDropChance")
    Float enemyBlueprintDropChance;

    @SerializedName("rarity")
    String rarity;

    @SerializedName("chance")
    Float chance;

}
