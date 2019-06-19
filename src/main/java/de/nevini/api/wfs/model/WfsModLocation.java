package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsModLocation {

    @SerializedName("_id")
    private final String id;

    @SerializedName("modName")
    private final String modName;

    @SerializedName("enemies")
    private final List<WfsEnemy> enemies;

}
