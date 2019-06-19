package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class WfsKey {

    @SerializedName("_id")
    private final String id;

    @SerializedName("keyName")
    private final String keyName;

    @SerializedName("rewards")
    private final Map<String, List<WfsItem>> rewards;

}
