package de.nevini.modules.osu.api.requests;

import de.nevini.modules.osu.api.OsuApi;
import lombok.Getter;
import okhttp3.OkHttpClient;

/**
 * You <b>must</b> have a valid {@code osu.token} set as a system property in order for these tests to work!
 */
class OsuApiProvider {

    @Getter
    private final OsuApi osuApi;

    OsuApiProvider() {
        osuApi = new OsuApi(new OkHttpClient.Builder().build(), System.getProperty("osu.token"));
    }

}
