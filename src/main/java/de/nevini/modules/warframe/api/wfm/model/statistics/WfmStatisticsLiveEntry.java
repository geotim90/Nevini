package de.nevini.modules.warframe.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmStatisticsLiveEntry {

    @SerializedName("datetime")
    OffsetDateTime dateTime;

    @SerializedName("volume")
    Integer volume;

    @SerializedName("min_price")
    Integer minPrice;

    @SerializedName("max_price")
    Integer maxPrice;

    @SerializedName("avg_price")
    Float avgPrice;

    @SerializedName("wa_price")
    Float waPrice;

    // note: not true median!
    @SerializedName("median")
    Float median;

    @SerializedName("order_type")
    String orderType;

    @SerializedName("moving_avg")
    Float movingAvg;

    @SerializedName("id")
    String id;

}
