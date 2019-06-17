package de.nevini.api.wfm.model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmOrder {

    @SerializedName("visible")
    private Boolean visible;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("creation_date")
    private OffsetDateTime creationDate;

    @SerializedName("user")
    private WfmUser user;

    @SerializedName("last_update")
    private OffsetDateTime lastUpdate;

    @SerializedName("platinum")
    private Integer platinum;

    @SerializedName("order_type")
    private String orderType;

    @SerializedName("region")
    private String region;

    @SerializedName("platform")
    private String platform;

    @SerializedName("id")
    private String id;

}
