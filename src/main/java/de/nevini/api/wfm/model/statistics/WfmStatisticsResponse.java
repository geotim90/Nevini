package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmStatisticsResponse {

    @SerializedName("payload")
    WfmStatisticsPayload payload;

}
