package de.nevini.bot.data.warframe.wfm;

import de.nevini.api.wfm.WarframeMarketApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WarframeMarketApiProvider {

    private final WarframeMarketApi api;

    public WarframeMarketApiProvider() {
        api = new WarframeMarketApi(new OkHttpClient.Builder().build());
    }

}
