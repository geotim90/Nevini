package de.nevini.api.osu.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class OsuMatch {

    @SerializedName("match")
    private final OsuMatchData match;

    @SerializedName("games")
    private final List<OsuGame> games;

}
