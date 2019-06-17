package de.nevini.api.wfm.model.items;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemsPayload {

    @SerializedName("items")
    private WfmItems items;

}
