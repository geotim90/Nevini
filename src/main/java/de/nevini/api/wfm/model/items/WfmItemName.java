package de.nevini.api.wfm.model.items;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemName {

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("id")
    private String id;

    @SerializedName("url_name")
    private String urlName;

}
