package de.nevini.modules.warframe.cache.wfw;

import de.nevini.modules.warframe.api.wfw.WarframeWikiApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WarframeWikiApiProvider {

    private final WarframeWikiApi api = new WarframeWikiApi(new OkHttpClient.Builder().build());

}
