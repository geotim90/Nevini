package de.nevini.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmStatisticsClosed {

    @SerializedName("48hours")
    private List<WfmStatisticsClosedEntry> last48hours;

    @SerializedName("90days")
    private List<WfmStatisticsClosedEntry> last90days;

}
