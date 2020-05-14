package de.nevini.api.wfs.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.WfsInfo;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfsInfoRequest implements WfsApiRequest<WfsInfo> {

    @Override
    public @NonNull String getEndpoint() {
        return "http://drops.warframestat.us/data/info.json";
    }

    @Override
    public @NonNull ApiResponse<WfsInfo> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    WfsInfo parseStream(Reader reader) {
        Type type = new TypeToken<WfsInfo>() {
        }.getType();
        return WfsJson.getGson().fromJson(reader, type);
    }

}
