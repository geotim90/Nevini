package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Value
public class WfsSortie {

    @SerializedName("boss")
    String boss;

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("variants")
    List<WfsSortieVariant> variants;

}
