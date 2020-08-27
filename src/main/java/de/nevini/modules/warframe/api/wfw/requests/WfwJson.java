package de.nevini.modules.warframe.api.wfw.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

class WfwJson {

    @Getter
    private static final Gson gson = new GsonBuilder().create();

}
