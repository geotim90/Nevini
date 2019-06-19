package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsEnemy {

    @SerializedName("_id")
    private final String id;

    @SerializedName("enemyName")
    private final String enemyName;

    @SerializedName("enemyModDropChance")
    private final Float enemyModDropChance;

    @SerializedName("enemyItemDropChance")
    private final Float enemyItemDropChance;

    @SerializedName("enemyBlueprintDropChance")
    private final Float enemyBlueprintDropChance;

    @SerializedName("rarity")
    private final String rarity;

    @SerializedName("chance")
    private final Float chance;

}
