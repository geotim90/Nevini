package de.nevini.api.osu.external.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.OsuApiReplay;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.Reader;

@Builder
@Value
public class OsuApiGetReplayRequest implements OsuApiRequest<OsuApiReplay> {

    @NonNull
    private final Integer mode;
    @NonNull
    private final Integer beatmapId;
    @NonNull
    private final String user;
    private final OsuUserType userType;
    private final Integer mods;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_replay";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("m", Integer.toString(mode));
        builder.addFormDataPart("b", Integer.toString(beatmapId));
        builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getId());
        if (mods != null) builder.addFormDataPart("mods", Integer.toString(mods));
        return builder.build();
    }

    @Override
    public @NonNull ApiResponse<OsuApiReplay> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    OsuApiReplay parseStream(Reader reader) {
        return OsuJson.getGson().fromJson(reader, OsuApiReplay.class);
    }

}
