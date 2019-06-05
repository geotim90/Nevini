package de.nevini.api.osu.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.adapters.OsuConverter;
import de.nevini.api.osu.model.OsuMod;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuScore;
import de.nevini.api.osu.model.OsuUserType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.lang.reflect.Type;
import java.util.List;

@Builder
@Value
public class OsuScoresRequest implements ApiRequest<List<OsuScore>> {

    @NonNull
    private final Integer beatmapId;
    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final OsuMod[] mods;
    private final Integer limit;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_scores";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("b", Integer.toString(beatmapId));
        if (user != null) builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getUserType());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode.getId()));
        if (mods != null) builder.addFormDataPart("mods", OsuConverter.convertModsToString(mods));
        if (limit != null) builder.addFormDataPart("limit", Integer.toString(limit));
        return builder.build();
    }

    @Override
    public ApiResponse<List<OsuScore>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                Type type = new TypeToken<List<OsuScore>>() {
                }.getType();
                return ApiResponse.ok(OsuJson.getGson().fromJson(body.charStream(), type));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

}
