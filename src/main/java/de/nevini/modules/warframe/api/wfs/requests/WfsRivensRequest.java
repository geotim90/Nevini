package de.nevini.modules.warframe.api.wfs.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRiven;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

@Builder
@Value
public class WfsRivensRequest implements WfsApiRequest<Map<String, Map<String, WfsRiven>>> {

    @Override
    public @NonNull String getEndpoint() {
        return "https://api.warframestat.us/pc/rivens";
    }

    @Override
    public @NonNull ApiResponse<Map<String, Map<String, WfsRiven>>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    Map<String, Map<String, WfsRiven>> parseStream(Reader reader) {
        Type type = new TypeToken<Map<String, Map<String, WfsRiven>>>() {
        }.getType();
        return WfsJson.getGson().fromJson(reader, type);
    }

}
