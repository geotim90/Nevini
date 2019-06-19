package de.nevini.api.wfs;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.WfsDrops;
import de.nevini.api.wfs.model.WfsInfo;
import de.nevini.api.wfs.requests.WfsApiRequest;
import de.nevini.api.wfs.requests.WfsDropsRequest;
import de.nevini.api.wfs.requests.WfsInfoRequest;
import de.nevini.util.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class WarframeStatsApi {

    // no rate limit restrictions documentation found - limit to 2/min to be safe
    private final TokenBucket rateLimit = new TokenBucket(2, 2, 2, TimeUnit.MINUTES);

    @NonNull
    private final OkHttpClient httpClient;

    private @NonNull <T> ApiResponse<T> call(@NonNull WfsApiRequest<T> apiRequest) {
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

    public ApiResponse<WfsDrops> getDrops(WfsDropsRequest request) {
        return call(request);
    }

    public ApiResponse<WfsInfo> getInfo(WfsInfoRequest request) {
        return call(request);
    }

}
