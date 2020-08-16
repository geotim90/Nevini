package de.nevini.api.wfs.model.weapons;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsFalloff {

    @SerializedName("start")
    Float start;

    @SerializedName("end")
    Float end;

    @SerializedName("reduction")
    Float reduction;

}
