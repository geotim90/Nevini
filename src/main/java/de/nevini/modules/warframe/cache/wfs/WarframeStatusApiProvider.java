package de.nevini.modules.warframe.cache.wfs;

import de.nevini.modules.warframe.api.wfs.WarframeStatusApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WarframeStatusApiProvider {

    private final WarframeStatusApi api = new WarframeStatusApi(new OkHttpClient.Builder().build());

}
