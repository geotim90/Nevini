package de.nevini.modules.osu.api.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Date;

@Value
public class OsuApiUserEvent {

    @SerializedName("display_html")
    String displayHtml;

    @SerializedName("beatmap_id")
    Integer beatmapId;

    @SerializedName("beatmapset_id")
    Integer beatmapsetId;

    @SerializedName("date")
    Date date;

    @SerializedName("epicfactor")
    Integer epicFactor;

}
