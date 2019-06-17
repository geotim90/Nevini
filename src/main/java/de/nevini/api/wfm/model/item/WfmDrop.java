package de.nevini.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmDrop {

    @SerializedName("name")
    private String name;

    @SerializedName("link")
    private String link;

}
