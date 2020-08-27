package de.nevini.modules.warframe.api.wfs.model.worldstate;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class WfsNightwave {

    @SerializedName("activeChallenges")
    List<WfsActiveChallenge> activeChallenges;

}
