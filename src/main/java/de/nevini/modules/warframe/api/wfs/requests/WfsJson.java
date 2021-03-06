package de.nevini.modules.warframe.api.wfs.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nevini.modules.warframe.api.wfs.adapters.OffsetDateTimeAdapter;
import de.nevini.modules.warframe.api.wfs.adapters.WfsIntegerAdapter;
import de.nevini.modules.warframe.api.wfs.adapters.WfsNceFloatAdapter;
import lombok.Getter;

import java.time.OffsetDateTime;

class WfsJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Float.class, new WfsNceFloatAdapter())
            .registerTypeAdapter(Integer.class, new WfsIntegerAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .create();

}
