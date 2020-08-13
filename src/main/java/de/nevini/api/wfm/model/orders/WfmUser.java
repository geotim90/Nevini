package de.nevini.api.wfm.model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmUser {

    @SerializedName("ingame_name")
    String name;

    @SerializedName("last_seen")
    OffsetDateTime lastSeen;

    @SerializedName("reputation_bonus")
    Integer reputationBonus;

    @SerializedName("reputation")
    Integer reputation;

    @SerializedName("region")
    String region;

    @SerializedName("status")
    String status;

    @SerializedName("id")
    String id;

    @SerializedName("avatar")
    String avatar;

}
