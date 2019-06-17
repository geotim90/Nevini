package de.nevini.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfmItemInformation {

    @SerializedName("item_name")
    private String itemName;

    @SerializedName("description")
    private String description;

    @SerializedName("wiki_link")
    private String wikiLink;

    @SerializedName("drop")
    private List<WfmDrop> drop;

}
