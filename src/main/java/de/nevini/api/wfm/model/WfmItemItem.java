package de.nevini.api.wfm.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItemItem {

    @SerializedName("items_in_set")
    private List<WfmItemInSet> itemsInSet;

}
