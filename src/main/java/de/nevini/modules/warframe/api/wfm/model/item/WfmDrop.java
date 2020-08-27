package de.nevini.modules.warframe.api.wfm.model.item;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfmDrop {

    @SerializedName("name")
    String name;

    @SerializedName("link")
    String link;

}
