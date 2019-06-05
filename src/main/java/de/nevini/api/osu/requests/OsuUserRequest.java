package de.nevini.api.osu.requests;

import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
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
public class OsuUserRequest implements ApiRequest<OsuUser> {

    @NonNull
    private final String user;
    private final OsuUserType userType;
    private final OsuMode mode;
    private final Integer eventDays;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_user";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getUserType());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode.getId()));
        if (eventDays != null) builder.addFormDataPart("event_days", Integer.toString(eventDays));
        return builder.build();
    }

    @Override
    public ApiResponse<OsuUser> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(OsuJson.getGson().fromJson(body.charStream(), OsuUser.class));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

}
