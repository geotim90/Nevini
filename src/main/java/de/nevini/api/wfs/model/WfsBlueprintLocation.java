package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsBlueprintLocation {

    @SerializedName("_id")
    private final String id;

    @SerializedName("itemName")
    private final String itemName;

    @SerializedName("blueprintName")
    private final String blueprintName;

    @SerializedName("enemies")
    private final List<WfsEnemy> enemies;

}
