package de.nevini.api.osu.requests;

import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.adapters.OsuConverter;
import de.nevini.api.osu.model.OsuMod;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuReplay;
import de.nevini.api.osu.model.OsuUserType;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Builder
@Value
public class OsuReplayRequest implements ApiRequest<OsuReplay> {

    @NonNull
    private final OsuMode mode;
    @NonNull
    private final Integer beatmapId;
    @NonNull
    private final String user;
    private final OsuUserType userType;
    private final OsuMod[] mods;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_replay";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("m", Integer.toString(mode.getId()));
        builder.addFormDataPart("b", Integer.toString(beatmapId));
        builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getUserType());
        if (mods != null) builder.addFormDataPart("mods", OsuConverter.convertModsToString(mods));
        return builder.build();
    }

    @Override
    public ApiResponse<OsuReplay> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(OsuJson.getGson().fromJson(body.charStream(), OsuReplay.class));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

}