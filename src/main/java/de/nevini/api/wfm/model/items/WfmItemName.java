package de.nevini.api.wfm.model.items;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemName {

    @SerializedName("item_name")
    String itemName;

    @SerializedName("thumb")
    String thumb;

    @SerializedName("id")
    String id;

    @SerializedName("url_name")
    String urlName;

}
