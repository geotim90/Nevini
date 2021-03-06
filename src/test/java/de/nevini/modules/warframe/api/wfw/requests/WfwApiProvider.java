package de.nevini.modules.warframe.api.wfw.requests;

import de.nevini.modules.warframe.api.wfw.WarframeWikiApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

class WfwApiProvider {

    @Getter
    private final WarframeWikiApi wfwApi;

    WfwApiProvider() {
        wfwApi = new WarframeWikiApi(new OkHttpClient.Builder().build());
    }

}
