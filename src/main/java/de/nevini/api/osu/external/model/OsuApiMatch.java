package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

@Value
public class OsuApiMatch {

    @SerializedName("match")
    OsuApiMatchMatch match;

    @SerializedName("games")
    List<OsuApiMatchGame> games;

}
