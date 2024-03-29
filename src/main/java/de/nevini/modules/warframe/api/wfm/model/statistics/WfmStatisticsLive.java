package de.nevini.modules.warframe.api.wfm.model.statistics;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmStatisticsLive {

    @SerializedName("48hours")
    List<WfmStatisticsLiveEntry> last48hours;

    @SerializedName("90days")
    List<WfmStatisticsLiveEntry> last90days;

}
