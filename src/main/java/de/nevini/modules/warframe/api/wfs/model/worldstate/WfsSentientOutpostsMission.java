package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsSentientOutpostsMission {

    @SerializedName("node")
    String node;

    @SerializedName("faction")
    String faction;

    @SerializedName("type")
    String type;

}
