package de.nevini.api.osu.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nevini.api.osu.adapters.*;
import de.nevini.api.osu.model.*;
import lombok.Getter;

import java.util.Date;

class OsuJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapter(OsuBeatmapApproved.class, new OsuBeatmapApprovedAdapter())
            .registerTypeAdapter(OsuBeatmapGenre.class, new OsuBeatmapGenreAdapter())
            .registerTypeAdapter(OsuBeatmapLanguage.class, new OsuBeatmapLanguageAdapter())
            .registerTypeAdapter(OsuMatchType.class, new OsuMatchTypeAdapter())
            .registerTypeAdapter(OsuMode.class, new OsuModeAdapter())
            .registerTypeAdapter(OsuMod[].class, new OsuModsAdapter())
            .registerTypeAdapter(OsuRank.class, new OsuRankAdapter())
            .registerTypeAdapter(OsuScoringType.class, new OsuScoringTypeAdapter())
            .registerTypeAdapter(OsuTeam.class, new OsuTeamAdapter())
            .registerTypeAdapter(OsuTeamType.class, new OsuTeamTypeAdapter())
            .registerTypeAdapter(String[].class, new StringsAdapter())
            .registerTypeAdapterFactory(new DefaultTypeAdapterFactory())
            .create();

}
