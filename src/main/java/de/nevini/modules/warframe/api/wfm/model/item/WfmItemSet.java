package de.nevini.modules.warframe.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItemSet {

    @SerializedName("items_in_set")
    List<WfmItem> itemsInSet;

}
