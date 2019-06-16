package de.nevini.api.wfm.requests;

import de.nevini.api.wfm.WarframeMarketApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfmApiProvider {

    @Getter
    private final WarframeMarketApi wfmApi;

    WfmApiProvider() {
        wfmApi = new WarframeMarketApi(new OkHttpClient.Builder().build());
    }

}
