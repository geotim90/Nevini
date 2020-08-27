package de.nevini.modules.osu.api.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nevini.modules.osu.api.adapters.BooleanAdapter;
import de.nevini.modules.osu.api.adapters.DateAdapter;
import de.nevini.modules.osu.api.adapters.DefaultTypeAdapterFactory;
import lombok.Getter;

import java.util.Date;

class OsuJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Boolean.class, new BooleanAdapter())
            .registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapterFactory(new DefaultTypeAdapterFactory())
            .create();

}
