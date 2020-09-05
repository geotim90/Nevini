package de.nevini.modules.warframe.api.wfm.model.items;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItemsPayload {

    @SerializedName("items")
    List<WfmItemName> items;

}
