package de.nevini.api.wfs.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.drops.WfsDrop;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

@Builder
@Value
public class WfsDropsRequest implements WfsApiRequest<List<WfsDrop>> {

    @Override
    public @NonNull String getEndpoint() {
        return "https://api.warframestat.us/drops";
    }

    @Override
    public @NonNull ApiResponse<List<WfsDrop>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    List<WfsDrop> parseStream(Reader reader) {
        Type type = new TypeToken<List<WfsDrop>>() {
        }.getType();
        return WfsJson.getGson().fromJson(reader, type);
    }

}
