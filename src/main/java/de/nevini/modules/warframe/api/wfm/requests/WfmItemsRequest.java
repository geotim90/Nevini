package de.nevini.modules.warframe.api.wfm.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemsResponse;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfmItemsRequest implements WfmApiRequest<WfmItemsResponse> {

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
