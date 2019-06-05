package de.nevini.api.osu.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.adapters.OsuConverter;
import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.api.osu.model.OsuMod;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUserType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuBeatmapsRequest implements ApiRequest<List<OsuBeatmap>> {

    private final Date since;
    private final Integer beatmapsetId;
    private final Integer beatmapId;
    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final Boolean includeConvertedBeatmaps;
    private final String beatmapHash;
    private final Integer limit;
    private final OsuMod[] mods;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_beatmaps";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        if (since != null) builder.addFormDataPart("since", OsuConverter.convertDateToString(since));
        if (beatmapsetId != null) builder.addFormDataPart("s", Integer.toString(beatmapsetId));
        if (beatmapId != null) builder.addFormDataPart("b", Integer.toString(beatmapId));
        if (user != null) builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getUserType());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode.getId()));
        if (includeConvertedBeatmaps != null) builder.addFormDataPart("a", includeConvertedBeatmaps ? "1" : "0");
        if (beatmapHash != null) builder.addFormDataPart("h", beatmapHash);
        if (limit != null) builder.addFormDataPart("limit", Integer.toString(limit));
        if (mods != null) builder.addFormDataPart("mods", OsuConverter.convertModsToString(mods));
        return builder.build();
    }

    @Override
    public ApiResponse<List<OsuBeatmap>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                Type type = new TypeToken<List<OsuBeatmap>>() {
                }.getType();
                return ApiResponse.ok(OsuJson.getGson().fromJson(body.charStream(), type));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

}
