package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmStatisticsClosedEntry {

    @SerializedName("datetime")
    private OffsetDateTime dateTime;

    @SerializedName("volume")
    private Integer volume;

    @SerializedName("min_price")
    private Integer minPrice;

    @SerializedName("max_price")
    private Integer maxPrice;

    @SerializedName("open_price")
    private Integer openPrice;

    @SerializedName("closed_price")
    private Integer closedPrice;

    @SerializedName("avg_price")
    private Float avgPrice;

    @SerializedName("wa_price")
    private Float waPrice;

    // note: not true median!
    @SerializedName("median")
    private Float median;

    @SerializedName("donch_top")
    private Integer donchTop;

    @SerializedName("donch_bot")
    private Integer donchBot;

    @SerializedName("id")
    private String id;

}
