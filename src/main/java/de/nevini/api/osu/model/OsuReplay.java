package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class OsuReplay {

    @SerializedName("content")
    private final String content;

    @SerializedName("encoding")
    private final String encoding;

}
