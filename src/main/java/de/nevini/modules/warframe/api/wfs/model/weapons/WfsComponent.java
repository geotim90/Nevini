package de.nevini.modules.warframe.api.wfs.model.weapons;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsComponent {

    @SerializedName("uniqueName")
    String uniqueName;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("itemCount")
    Integer itemCount;

    @SerializedName("imageName")
    String imageName;

    @SerializedName("tradable")
    Boolean tradable;

    @SerializedName("drops")
    List<WfsDrop> drops;

}
