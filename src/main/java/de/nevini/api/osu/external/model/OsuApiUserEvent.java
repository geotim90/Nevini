package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiUserEvent {

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
