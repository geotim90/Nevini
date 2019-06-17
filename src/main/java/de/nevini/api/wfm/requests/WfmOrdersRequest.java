package de.nevini.api.wfm.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.orders.WfmOrdersResponse;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfmOrdersRequest implements ApiRequest<WfmOrdersResponse> {

    @NonNull
    private final String itemUrlName;

    @Override
    public @NonNull String getEndpoint() {
        return "https://api.warframe.market/v1/items/" + itemUrlName + "/orders";
    }

    @Override
    public @NonNull ApiResponse<WfmOrdersResponse> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    WfmOrdersResponse parseStream(Reader reader) {
        Type type = new TypeToken<WfmOrdersResponse>() {
        }.getType();
        return WfmJson.getGson().fromJson(reader, type);
    }

}