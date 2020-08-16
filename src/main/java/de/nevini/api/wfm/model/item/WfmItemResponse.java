package de.nevini.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemResponse {

    @SerializedName("payload")
    WfmItemPayload payload;

}
