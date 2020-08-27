package de.nevini.modules.warframe.api.wfs.model.weapons;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsAreaAttack {

    @SerializedName("name")
    String name;

    @SerializedName("falloff")
    WfsFalloff falloff;

    @SerializedName("impact")
    Float impact;

    @SerializedName("puncture")
    Float puncture;

    @SerializedName("slash")
    Float slash;

    @SerializedName("heat")
    Float heat;

    @SerializedName("cold")
    Float cold;

    @SerializedName("electric")
    Float electric;

    @SerializedName("toxin")
    Float toxin;

    @SerializedName("gas")
    Float gas;

    @SerializedName("viral")
    Float viral;

    @SerializedName("corrosive")
    Float corrosive;

    @SerializedName("blast")
    Float blast;

    @SerializedName("magnetic")
    Float magnetic;

    @SerializedName("radiation")
    Float radiation;

    @SerializedName("true")
    Float trueDamage;

    @SerializedName("void")
    Float voidDamage;

}
