package de.nevini.api.wfs.requests;

import de.nevini.api.wfs.WarframeStatusApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfsApiProvider {

    @Getter
    private final WarframeStatusApi wfsApi;

    WfsApiProvider() {
        wfsApi = new WarframeStatusApi(new OkHttpClient.Builder().build());
    }

}
