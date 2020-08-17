package de.nevini.api.wfw;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfw.model.UnexpandedListArticleResultSet;
import de.nevini.api.wfw.requests.WfwApiRequest;
import de.nevini.api.wfw.requests.WfwArticleRequest;
import de.nevini.util.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class WarframeWikiaApi {

    // no rate limit restrictions documentation found - limit to 1/hour
    private final TokenBucket rateLimit = new TokenBucket(1, 1, 1, TimeUnit.HOURS);

    @NonNull
    private final OkHttpClient httpClient;

    private @NonNull <T> ApiResponse<T> call(@NonNull WfwApiRequest<T> apiRequest, @NonNull TokenBucket rateLimit) {
        log.debug("Request: {}", apiRequest);
        // check rate limits
        if (rateLimit.requestToken()) {
            // prepare the GET request
            Request request = apiRequest.getRequest(new Request.Builder()
                    .url(apiRequest.getEndpoint())
                    .get());
            try {
                // make the call and parse the result
                return apiRequest.parseResponse(httpClient.newCall(request).execute());
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

    public ApiResponse<UnexpandedListArticleResultSet> getArticles(WfwArticleRequest request) {
        return call(request, rateLimit);
    }

}
