package de.nevini.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItemInformation {

    @SerializedName("item_name")
    String itemName;

    @SerializedName("description")
    String description;

    @SerializedName("wiki_link")
    String wikiLink;

    @SerializedName("drop")
    List<WfmDrop> drop;

}
