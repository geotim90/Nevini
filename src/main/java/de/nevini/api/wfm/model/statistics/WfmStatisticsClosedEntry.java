package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfmStatisticsClosedEntry {

    @SerializedName("datetime")
    OffsetDateTime dateTime;

    @SerializedName("volume")
    Integer volume;

    @SerializedName("min_price")
    Integer minPrice;

    @SerializedName("max_price")
    Integer maxPrice;

    @SerializedName("open_price")
    Integer openPrice;

    @SerializedName("closed_price")
    Integer closedPrice;

    @SerializedName("avg_price")
    Float avgPrice;

    @SerializedName("wa_price")
    Float waPrice;

    // note: not true median!
    @SerializedName("median")
    Float median;

    @SerializedName("donch_top")
    Integer donchTop;

    @SerializedName("donch_bot")
    Integer donchBot;

    @SerializedName("id")
    String id;

}
