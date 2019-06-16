package de.nevini.api.wfm.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemsResponse {

    @SerializedName("payload")
    private WfmItemsPayload payload;

}
