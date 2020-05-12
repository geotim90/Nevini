package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsInfo {

    @SerializedName("hash")
    String hash;

    @SerializedName("timestamp")
    Long timestamp;

    @SerializedName("modified")
    Long modified;

}
