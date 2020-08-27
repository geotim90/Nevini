package de.nevini.modules.warframe.api.wfs.model.weapons;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class WfsWeapon {

    @SerializedName("name")
    String name;

    @SerializedName("uniqueName")
    String uniqueName;

    @SerializedName("damagePerShot")
    Float[] damagePerShot;

    @SerializedName("totalDamage")
    Float totalDamage;

    @SerializedName("description")
    String description;

    @SerializedName("criticalChance")
    Float criticalChance;

    @SerializedName("criticalMultiplier")
    Float criticalMultiplier;

    @SerializedName("procChance")
    Float procChance;

    @SerializedName("fireRate")
    Float fireRate;

    @SerializedName("masteryReq")
    Integer masteryReq;

    @SerializedName("productCategory")
    String productCategory;

    @SerializedName("slot")
    Integer slot;

    @SerializedName("accuracy")
    Float accuracy;

    @SerializedName("omegaAttenuation")
    Float omegaAttenuation;

    @SerializedName("noise")
    String noise;

    @SerializedName("trigger")
    String trigger;

    @SerializedName("magazineSize")
    Integer magazineSize;

    @SerializedName("reloadTime")
    Float reloadTime;

    @SerializedName("multishot")
    Integer multishot;

    @SerializedName("buildPrice")
    Integer buildPrice;

    @SerializedName("buildTime")
    Integer buildTime;

    @SerializedName("skipBuildTimePrice")
    Integer skipBuildTimePrice;

    @SerializedName("buildQuantity")
    Integer buildQuantity;

    @SerializedName("consumeOnBuild")
    Boolean consumeOnBuild;

    @SerializedName("components")
    List<WfsComponent> components;

    @SerializedName("type")
    String type;

    @SerializedName("imageName")
    String imageName;

    @SerializedName("category")
    String category;

    @SerializedName("tradable")
    Boolean tradable;

    @SerializedName("patchlogs")
    List<WfsPatchlog> patchlogs;

    @SerializedName("ammo")
    Integer ammo;

    @SerializedName("areaAttack")
    WfsAreaAttack areaAttack;

    @SerializedName("damage")
    String damage;

    @SerializedName("damageTypes")
    Map<String, Float> damageTypes;

    @SerializedName("flight")
    Integer flight;

    @SerializedName("marketCost")
    Integer marketCost;

    @SerializedName("polarities")
    String[] polarities;

    @SerializedName("stancePolarity")
    String stancePolarity;

    @SerializedName("projectile")
    String projectile;

    @SerializedName("tags")
    String[] tags;

    @SerializedName("wikiaThumbnail")
    String wikiaThumbnail;

    @SerializedName("wikiaUrl")
    String wikiaUrl;

    @SerializedName("disposition")
    Integer disposition;

}
