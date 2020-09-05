package de.nevini.modules.osu.api.requests;

import com.google.gson.reflect.TypeToken;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.osu.api.model.OsuApiUser;
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
public class OsuApiGetUserRequest implements OsuApiRequest<List<OsuApiUser>> {

    @NonNull
    String user;
    OsuUserType userType;
    Integer mode;
    Integer eventDays;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_user";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("u", user);
        if (userType != null) builder.addFormDataPart("type", userType.getId());
        if (mode != null) builder.addFormDataPart("m", Integer.toString(mode));
        if (eventDays != null) builder.addFormDataPart("event_days", Integer.toString(eventDays));
        return builder.build();
    }

    @Override
    public @NonNull ApiResponse<List<OsuApiUser>> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    List<OsuApiUser> parseStream(Reader reader) {
        Type type = new TypeToken<List<OsuApiUser>>() {
        }.getType();
        return OsuJson.getGson().fromJson(reader, type);
    }

}
