package de.nevini.api.wfs.requests;

import de.nevini.api.wfs.WarframeStatsApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfsApiProvider {

    @Getter
    private final WarframeStatsApi wfsApi;

    WfsApiProvider() {
        wfsApi = new WarframeStatsApi(new OkHttpClient.Builder().build());
    }

}
