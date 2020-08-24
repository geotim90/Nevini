package de.nevini.api.wfs;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.model.drops.WfsDrop;
import de.nevini.api.wfs.model.rivens.WfsRiven;
import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.requests.*;
import de.nevini.util.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class WarframeStatsApi {

    // no rate limit restrictions documentation found - limit to 1/min per cached endpoint
    private final TokenBucket rateLimitRivens = new TokenBucket(1, 1, 1, TimeUnit.MINUTES);
    private final TokenBucket rateLimitWeapons = new TokenBucket(1, 1, 1, TimeUnit.MINUTES);
    private final TokenBucket rateLimitWorldState = new TokenBucket(1, 1, 1, TimeUnit.MINUTES);

    // no rate limit restrictions documentation found - limit to 1/sec per uncached endpoint
    private final TokenBucket rateLimitDrops = new TokenBucket(1, 1, 1, TimeUnit.SECONDS);

    @NonNull
    private final OkHttpClient httpClient;

    private @NonNull <T> ApiResponse<T> call(@NonNull WfsApiRequest<T> apiRequest, @NonNull TokenBucket rateLimit) {
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

    public ApiResponse<List<WfsDrop>> getDrops(WfsDropsRequest request) {
        return call(request, rateLimitDrops);
    }

    public ApiResponse<Map<String, Map<String, WfsRiven>>> getRivens(WfsRivensRequest request) {
        return call(request, rateLimitRivens);
    }

    public ApiResponse<List<WfsWeapon>> getWeapons(WfsWeaponsRequest request) {
        return call(request, rateLimitWeapons);
    }

    public ApiResponse<WfsWorldState> getWorldState(WfsWorldStateRequest request) {
        return call(request, rateLimitWorldState);
    }

}
