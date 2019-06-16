package de.nevini.api.osu.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUserBest;
import de.nevini.api.osu.model.OsuUserType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

@Builder
@Value
public class OsuUserBestRequest implements ApiRequest<List<OsuUserBest>> {

    @NonNull
    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final Integer limit;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_user_best";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getUserType());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode.getId()));
        if (limit != null) builder.addFormDataPart("limit", Integer.toString(limit));
        return builder.build();
    }

    @Override
    public @NonNull ApiResponse<List<OsuUserBest>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    List<OsuUserBest> parseStream(Reader reader) {
        Type type = new TypeToken<List<OsuUserBest>>() {
        }.getType();
        return OsuJson.getGson().fromJson(reader, type);
    }

}
