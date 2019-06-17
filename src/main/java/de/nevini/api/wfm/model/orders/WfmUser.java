package de.nevini.api.wfm.model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmUser {

    @SerializedName("ingame_name")
    private String name;

    @SerializedName("last_seen")
    private OffsetDateTime lastSeen;

    @SerializedName("reputation_bonus")
    private Integer reputationBonus;

    @SerializedName("reputation")
    private Integer reputation;

    @SerializedName("region")
    private String region;

    @SerializedName("status")
    private String status;

    @SerializedName("id")
    private String id;

    @SerializedName("avatar")
    private String avatar;

}
