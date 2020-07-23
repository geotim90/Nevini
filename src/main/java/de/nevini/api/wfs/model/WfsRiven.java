package de.nevini.api.wfs.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.ObjectUtils;

@Builder
@Value
public class WfsRiven {

    @SerializedName("rerolled")
    WfsRivenData rerolled;

    @SerializedName("unrolled")
    WfsRivenData unrolled;

    public String getDisplayName() {
        WfsRivenData data = ObjectUtils.defaultIfNull(unrolled, rerolled);
        if (data != null && data.getCompatibility() != null && !data.getCompatibility().startsWith("Veiled")) {
            return data.getCompatibility() + " " + data.getItemType();
        } else {
            return data == null ? null : data.getCompatibility();
        }
    }

}
