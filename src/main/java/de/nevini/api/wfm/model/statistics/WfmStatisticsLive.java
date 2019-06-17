package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmStatisticsLive {

    @SerializedName("48hours")
    private List<WfmStatisticsLiveEntry> last48hours;

    @SerializedName("90days")
    private List<WfmStatisticsLiveEntry> last90days;

}
