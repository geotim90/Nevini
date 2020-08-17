package de.nevini.api.wfw.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfw.model.UnexpandedListArticleResultSet;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;

@Builder
@Value
public class WfwArticleRequest implements WfwApiRequest<UnexpandedListArticleResultSet> {

    @Override
    public @NonNull String getEndpoint() {
        return "https://warframe.fandom.com/api/v1/Articles/List?limit=999999";
    }

    @Override
    public @NonNull ApiResponse<UnexpandedListArticleResultSet> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    UnexpandedListArticleResultSet parseStream(Reader reader) {
        Type type = new TypeToken<UnexpandedListArticleResultSet>() {
        }.getType();
        return WfwJson.getGson().fromJson(reader, type);
    }

}
