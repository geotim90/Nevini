package de.nevini.api.wfw.requests;

import de.nevini.api.wfw.WarframeWikiaApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfwApiProvider {

    @Getter
    private final WarframeWikiaApi wfwApi;

    WfwApiProvider() {
        wfwApi = new WarframeWikiaApi(new OkHttpClient.Builder().build());
    }

}
