package de.nevini.api.wfm.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemPayload {

    @SerializedName("item")
    private WfmItemItem item;

}
