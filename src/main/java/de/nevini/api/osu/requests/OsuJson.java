package de.nevini.api.osu.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nevini.api.osu.adapters.*;
import de.nevini.api.osu.model.OsuBeatmapApproved;
import de.nevini.api.osu.model.OsuBeatmapGenre;
import de.nevini.api.osu.model.OsuBeatmapLanguage;
import de.nevini.api.osu.model.OsuMode;
import lombok.Getter;

import java.util.Date;

class OsuJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapter(OsuBeatmapApproved.class, new OsuBeatmapApprovedAdapter())
            .registerTypeAdapter(OsuBeatmapGenre.class, new OsuBeatmapGenreAdapter())
            .registerTypeAdapter(OsuBeatmapLanguage.class, new OsuBeatmapLanguageAdapter())
            .registerTypeAdapter(OsuMode.class, new OsuModeAdapter())
            .create();

}
