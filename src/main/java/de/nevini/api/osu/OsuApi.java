package de.nevini.api.osu;

import de.nevini.api.Api;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.model.*;
import de.nevini.api.osu.requests.*;
import de.nevini.commons.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class OsuApi implements Api {

    private final TokenBucket rateLimit = new TokenBucket(60, 60, 1200, TimeUnit.MINUTES);
    private final TokenBucket replayRateLimit = new TokenBucket(10, 10, 10, TimeUnit.MINUTES);

    @NonNull
    private final OkHttpClient httpClient;

    @NonNull
    private final String token;

    public <T> @NonNull ApiResponse<T> call(@NonNull ApiRequest<T> apiRequest) {
        // check rate limits
        if ((!(apiRequest instanceof OsuReplayRequest) || replayRateLimit.requestToken())
                && rateLimit.requestToken()
        ) {
            // prepare the body of the request
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("k", token);
            // prepare the POST request
            Request request = apiRequest.getRequest(new Request.Builder()
                    .url(apiRequest.getEndpoint())
                    .post(apiRequest.getRequestBody(builder)));
            try {
                // make the call and parse the result
                return apiRequest.parseResponse(httpClient.newCall(request).execute());
            } catch (Throwable throwable) {
                // an error occurred
                return ApiResponse.error(throwable);
            }
        } else {
            // call exceeds rate limit
            return ApiResponse.rateLimited();
        }
    }

    public @NonNull ApiResponse<List<OsuBeatmap>> getBeatmaps(@NonNull OsuBeatmapsRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuUser>> getUser(@NonNull OsuUserRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuScore>> getScores(@NonNull OsuScoresRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuUserBest>> getUserBest(@NonNull OsuUserBestRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuUserRecent>> getUserRecent(@NonNull OsuUserRecentRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<OsuMatch> getMatch(@NonNull OsuMatchRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<OsuReplay> getReplay(@NonNull OsuReplayRequest request) {
        return call(request);
    }

}
