package de.nevini.api.wfs.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.drops.WfsDrops;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfsDropsRequest implements WfsApiRequest<WfsDrops> {

    @Override
    public @NonNull String getEndpoint() {
        return "http://drops.warframestat.us/data/all.json";
    }

    @Override
    public @NonNull ApiResponse<WfsDrops> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    WfsDrops parseStream(Reader reader) {
        Type type = new TypeToken<WfsDrops>() {
        }.getType();
        return WfsJson.getGson().fromJson(reader, type);
    }

}
