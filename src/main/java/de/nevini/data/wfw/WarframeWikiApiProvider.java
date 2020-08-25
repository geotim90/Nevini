package de.nevini.data.wfw;

import de.nevini.api.wfw.WarframeWikiApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WarframeWikiApiProvider {

    private final WarframeWikiApi api = new WarframeWikiApi(new OkHttpClient.Builder().build());

}
