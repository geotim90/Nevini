package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class OsuApiReplay {

    @SerializedName("content")
    String content;

    @SerializedName("encoding")
    String encoding;

}
