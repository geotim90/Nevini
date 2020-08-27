package de.nevini.modules.warframe.api.wfm.model.orders;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmOrdersResponse {

    @SerializedName("payload")
    WfmOrdersPayload payload;

}
