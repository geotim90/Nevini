package de.nevini.data.wfs;

import de.nevini.api.wfs.WarframeStatusApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WarframeStatusApiProvider {

    private final WarframeStatusApi api = new WarframeStatusApi(new OkHttpClient.Builder().build());

}
