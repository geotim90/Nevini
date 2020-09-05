package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsDailyDeal {

    @SerializedName("item")
    String item;

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("originalPrice")
    Integer originalPrice;

    @SerializedName("salePrice")
    Integer salePrice;

    @SerializedName("total")
    Integer total;

    @SerializedName("sold")
    Integer sold;

    @SerializedName("eta")
    String eta;

    @SerializedName("discount")
    Integer discount;

}
