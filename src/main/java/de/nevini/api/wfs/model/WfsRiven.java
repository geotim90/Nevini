package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class WfsRiven {

    @SerializedName("rerolled")
    WfsRivenData rerolled;

    @SerializedName("unrolled")
    WfsRivenData unrolled;

    public String getDisplayName() {
        if (unrolled.getCompatibility().startsWith("Veiled")) {
            return unrolled.getCompatibility();
        } else {
            return unrolled.getCompatibility() + " " + unrolled.getItemType();
        }
    }

}
