package de.nevini.modules.warframe.api.wfs.model.weapons;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsPatchlog {

    @SerializedName("name")
    String name;

    @SerializedName("date")
    OffsetDateTime date;

    @SerializedName("url")
    String url;

    @SerializedName("imgUrl")
    String imgUrl;

    @SerializedName("additions")
    String additions;

    @SerializedName("changes")
    String changes;

    @SerializedName("fixes")
    String fixes;

}
