package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class WfsConclaveChallenge {

    @SerializedName("activation")
    OffsetDateTime activation;

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("mode")
    String mode;

    @SerializedName("rootChallenge")
    Boolean rootChallenge;

    @SerializedName("asString")
    String asString;

}
