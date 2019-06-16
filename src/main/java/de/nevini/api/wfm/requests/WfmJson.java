package de.nevini.api.wfm.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

class WfmJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .create();

}
