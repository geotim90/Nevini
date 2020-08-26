package de.nevini.api.wfw.requests;

import de.nevini.api.wfw.WarframeWikiApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfwApiProvider {

    @Getter
    private final WarframeWikiApi wfwApi;

    WfwApiProvider() {
        wfwApi = new WarframeWikiApi(new OkHttpClient.Builder().build());
    }

}
