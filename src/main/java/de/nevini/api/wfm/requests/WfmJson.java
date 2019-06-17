package de.nevini.api.wfm.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nevini.api.wfm.adapters.OffsetDateTimeAdapter;
import lombok.Getter;

import java.time.OffsetDateTime;

class WfmJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .create();

}
