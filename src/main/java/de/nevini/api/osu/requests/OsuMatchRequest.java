package de.nevini.api.osu.requests;

import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.OsuMatch;
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
public class OsuMatchRequest implements ApiRequest<OsuMatch> {

    @NonNull
    private final Integer matchId;

    @Override
    public @NonNull String getEndpoint() {
        return "https://osu.ppy.sh/api/get_match";
    }

    @Override
    public @NonNull RequestBody getRequestBody(@NonNull MultipartBody.Builder builder) {
        builder.addFormDataPart("mp", Integer.toString(matchId));
        return builder.build();
    }

    @Override
    public ApiResponse<OsuMatch> parseResponse(@NonNull Response response) {
        try (ResponseBody body = response.body()) {
            if (body != null) {
                return ApiResponse.ok(parseStream(body.charStream()));
            }
        }
        // no parsable result
        return ApiResponse.empty();
    }

    OsuMatch parseStream(Reader reader) {
        return OsuJson.getGson().fromJson(reader, OsuMatch.class);
    }

}
