package de.nevini.api.wfs.model.rivens;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsRivenData {

    @SerializedName("itemType")
    String itemType;

    @SerializedName("compatibility")
    String compatibility;

    @SerializedName("rerolled")
    Boolean rerolled;

    @SerializedName("avg")
    Float avg;

    @SerializedName("stddev")
    Float stddev;

    @SerializedName("min")
    Integer min;

    @SerializedName("max")
    Integer max;

    @SerializedName("pop")
    Integer pop;

    @SerializedName("median")
    Float median;

}
