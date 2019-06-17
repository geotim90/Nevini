package de.nevini.api.wfm.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.WfmItemsResponse;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfmItemsRequest implements ApiRequest<WfmItemsResponse> {

    @Override
    public @NonNull String getEndpoint() {
        return "https://api.warframe.market/v1/items";
    }

    @Override
    public @NonNull ApiResponse<WfmItemsResponse> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    WfmItemsResponse parseStream(Reader reader) {
        Type type = new TypeToken<WfmItemsResponse>() {
        }.getType();
        return WfmJson.getGson().fromJson(reader, type);
    }

}