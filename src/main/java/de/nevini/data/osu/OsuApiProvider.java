package de.nevini.data.osu;

import de.nevini.api.osu.OsuApi;
import lombok.Getter;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OsuApiProvider {

    private final OsuApi api;

    public OsuApiProvider(@Value("${osu.token:#{null}}") String token) {
        api = new OsuApi(new OkHttpClient.Builder().build(), token);
    }

}
