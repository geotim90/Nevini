package de.nevini.api.osu.external.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

@Value
public class OsuApiMatch {

    @SerializedName("match")
    private final OsuApiMatchMatch match;

    @SerializedName("games")
    private final List<OsuApiMatchGame> games;

}
