package de.nevini.api.wfs.model.drops;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsModLocation {

    @SerializedName("_id")
    String id;

    @SerializedName("modName")
    String modName;

    @SerializedName("enemies")
    List<WfsEnemy> enemies;

}
