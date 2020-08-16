package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmStatisticsPayload {

    @SerializedName("statistics_closed")
    WfmStatisticsClosed closed;

    @SerializedName("statistics_live")
    WfmStatisticsLive live;

}
