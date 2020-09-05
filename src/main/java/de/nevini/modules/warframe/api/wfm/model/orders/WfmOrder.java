package de.nevini.modules.warframe.api.wfm.model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmOrder {

    @SerializedName("visible")
    Boolean visible;

    @SerializedName("quantity")
    Integer quantity;

    @SerializedName("creation_date")
    OffsetDateTime creationDate;

    @SerializedName("user")
    WfmUser user;

    @SerializedName("last_update")
    OffsetDateTime lastUpdate;

    @SerializedName("platinum")
    Integer platinum;

    @SerializedName("order_type")
    String orderType;

    @SerializedName("region")
    String region;

    @SerializedName("platform")
    String platform;

    @SerializedName("id")
    String id;

}
