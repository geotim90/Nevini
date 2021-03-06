package de.nevini.modules.osu.api.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.osu.api.adapters.DateAdapter;
import de.nevini.modules.osu.api.model.OsuApiBeatmap;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Builder
@Value
public class OsuApiGetBeatmapsRequest implements OsuApiRequest<List<OsuApiBeatmap>> {

    Date since;
    Integer beatmapsetId;
    Integer beatmapId;
    String user;
    OsuUserType userType;
    Integer mode;
    Boolean includeConvertedBeatmaps;
    String beatmapHash;
    Integer limit;
    Integer mods;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_beatmaps";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        if (since != null) builder.addFormDataPart("since", DateAdapter.convertDateToString(since));
        if (beatmapsetId != null) builder.addFormDataPart("s", Integer.toString(beatmapsetId));
        if (beatmapId != null) builder.addFormDataPart("b", Integer.toString(beatmapId));
        if (user != null) builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getId());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode));
        if (includeConvertedBeatmaps != null) builder.addFormDataPart("a", includeConvertedBeatmaps ? "1" : "0");
        if (beatmapHash != null) builder.addFormDataPart("h", beatmapHash);
        if (limit != null) builder.addFormDataPart("limit", Integer.toString(limit));
        if (mods != null) builder.addFormDataPart("mods", Integer.toString(mods));
        return builder.build();
    }

    @Override
    public @NonNull ApiResponse<List<OsuApiBeatmap>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    List<OsuApiBeatmap> parseStream(Reader reader) {
        Type type = new TypeToken<List<OsuApiBeatmap>>() {
        }.getType();
        return OsuJson.getGson().fromJson(reader, type);
    }

}
