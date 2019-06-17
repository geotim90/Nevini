package de.nevini.api.wfm.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmItemDrop {

    @SerializedName("name")
    private String name;

    @SerializedName("link")
    private String link;

}
