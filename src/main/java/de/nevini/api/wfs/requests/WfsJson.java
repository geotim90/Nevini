package de.nevini.api.wfs.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nevini.api.wfs.adapters.WfsNceFloatAdapter;
import de.nevini.api.wfs.adapters.WfsRewardsDeserializer;
import de.nevini.api.wfs.model.WfsRewards;
import lombok.Getter;

class WfsJson {

    @Getter
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Float.class, new WfsNceFloatAdapter())
            .registerTypeAdapter(WfsRewards.class, new WfsRewardsDeserializer())
            .create();

}