package de.nevini.api.wfs.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfsWorldStateRequest implements WfsApiRequest<WfsWorldState> {

    @Override
    public @NonNull String getEndpoint() {
        return "https://api.warframestat.us/pc";
    }

    @Override
    public @NonNull ApiResponse<WfsWorldState> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    WfsWorldState parseStream(Reader reader) {
        Type type = new TypeToken<WfsWorldState>() {
        }.getType();
        return WfsJson.getGson().fromJson(reader, type);
    }

}
