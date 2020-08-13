package de.nevini.api.osu.external;

import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.model.*;
import de.nevini.api.osu.external.requests.*;
import de.nevini.util.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class OsuApi {

    private final TokenBucket rateLimit = new TokenBucket(60, 60, 1200, TimeUnit.MINUTES);
    private final TokenBucket replayRateLimit = new TokenBucket(10, 10, 10, TimeUnit.MINUTES);

    @NonNull
    private final OkHttpClient httpClient;

    private final String token;

    private <T> @NonNull ApiResponse<T> call(@NonNull OsuApiRequest<T> apiRequest) {
        log.debug("Request: {}", apiRequest);
        // check token
        if (StringUtils.isEmpty(token)) {
            log.debug("Ignoring request due to missing token");
            return ApiResponse.empty();
        }
        // check rate limits
        if ((!(apiRequest instanceof OsuApiGetReplayRequest) || replayRateLimit.requestToken())
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
                ApiResponse<T> response = apiRequest.parseResponse(httpClient.newCall(request).execute());
                if (log.isDebugEnabled() && !response.isEmpty() && response.getResult() instanceof Collection) {
                    log.debug("Request successful with " + ((Collection) response.getResult()).size() + " results");
                } else {
                    log.debug("Request successful with {} result", response.isEmpty() ? "empty" : "non-empty");
                }
                return response;
            } catch (Throwable throwable) {
                // an error occurred
                log.info("Request failed: {}", apiRequest, throwable);
                return ApiResponse.error(throwable);
            }
        } else {
            // call exceeds rate limit
            log.info("Request rate limited: {}", apiRequest);
            return ApiResponse.rateLimited();
        }
    }

    public @NonNull ApiResponse<List<OsuApiBeatmap>> getBeatmaps(@NonNull OsuApiGetBeatmapsRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuApiUser>> getUser(@NonNull OsuApiGetUserRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuApiScore>> getScores(@NonNull OsuApiGetScoresRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuApiUserBest>> getUserBest(@NonNull OsuApiGetUserBestRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<List<OsuApiUserRecent>> getUserRecent(@NonNull OsuApiGetUserRecentRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<OsuApiMatch> getMatch(@NonNull OsuApiGetMatchRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<OsuApiReplay> getReplay(@NonNull OsuApiGetReplayRequest request) {
        return call(request);
    }

}
