package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsInfo {

    @SerializedName("hash")
    private final String hash;

    @SerializedName("timestamp")
    private final Long timestamp;

    @SerializedName("modified")
    private final Long modified;

}
