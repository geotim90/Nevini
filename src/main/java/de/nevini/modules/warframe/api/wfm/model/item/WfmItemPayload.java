package de.nevini.modules.warframe.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemPayload {

    @SerializedName("item")
    WfmItemSet item;

}
