package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmStatisticsLiveEntry {

    @SerializedName("datetime")
    private OffsetDateTime dateTime;

    @SerializedName("volume")
    private Integer volume;

    @SerializedName("min_price")
    private Integer minPrice;

    @SerializedName("max_price")
    private Integer maxPrice;

    @SerializedName("avg_price")
    private Float avgPrice;

    @SerializedName("wa_price")
    private Float waPrice;

    // note: not true median!
    @SerializedName("median")
    private Float median;

    @SerializedName("order_type")
    private String orderType;

    @SerializedName("moving_avg")
    private Float movingAvg;

    @SerializedName("id")
    private String id;

}
