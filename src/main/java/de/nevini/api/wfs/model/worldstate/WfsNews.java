package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsNews {

    @SerializedName("message")
    String message;

    @SerializedName("link")
    String link;

    @SerializedName("date")
    OffsetDateTime date;

    @SerializedName("eta")
    String eta;

    @SerializedName("asString")
    String asString;

}
