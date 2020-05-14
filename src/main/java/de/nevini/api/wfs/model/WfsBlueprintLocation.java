package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsBlueprintLocation {

    @SerializedName("_id")
    String id;

    @SerializedName("itemName")
    String itemName;

    @SerializedName("blueprintName")
    String blueprintName;

    @SerializedName("enemies")
    List<WfsEnemy> enemies;

}
