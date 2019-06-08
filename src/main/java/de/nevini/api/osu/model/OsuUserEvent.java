package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class OsuUserEvent {

    @SerializedName("display_html")
    private final String displayHtml;

    @SerializedName("beatmap_id")
    private final Integer beatmapId;

    @SerializedName("beatmapset_id")
    private final Integer beatmapsetId;

    @SerializedName("date")
    private final Date date;

    @SerializedName("epicfactor")
    private final Integer epicFactor;

}
