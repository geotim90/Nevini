package de.nevini.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Value
public class WfsSyndicateMissions {

    @SerializedName("expiry")
    OffsetDateTime expiry;

    @SerializedName("syndicate")
    String syndicate;

    @SerializedName("nodes")
    List<String> nodes;

    @SerializedName("jobs")
    List<WfsJob> jobs;

}
