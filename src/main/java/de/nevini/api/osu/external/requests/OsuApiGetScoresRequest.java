package de.nevini.api.osu.external.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.OsuApiScore;
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
public class OsuApiGetScoresRequest implements OsuApiRequest<List<OsuApiScore>> {

    @NonNull
    private final Integer beatmapId;
    private final String user;
    private final OsuUserType userType;
    private final Integer mode;
    private final Integer mods;
    private final Integer limit;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_scores";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("b", Integer.toString(beatmapId));
        if (user != null) builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getId());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode));
        if (mods != null) builder.addFormDataPart("mods", Integer.toString(mods));
        if (limit != null) builder.addFormDataPart("limit", Integer.toString(limit));
        return builder.build();
    }

    @Override
    public @NonNull ApiResponse<List<OsuApiScore>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    List<OsuApiScore> parseStream(Reader reader) {
        Type type = new TypeToken<List<OsuApiScore>>() {
        }.getType();
        return OsuJson.getGson().fromJson(reader, type);
    }

}
