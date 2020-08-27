package de.nevini.modules.warframe.api.wfs.requests;

import de.nevini.modules.warframe.api.wfs.WarframeStatusApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfsApiProvider {

    @Getter
    private final WarframeStatusApi wfsApi;

    WfsApiProvider() {
        wfsApi = new WarframeStatusApi(new OkHttpClient.Builder().build());
    }

}
