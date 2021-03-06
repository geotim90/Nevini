package de.nevini.modules.warframe.api.wfm;

import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfm.model.item.WfmItemResponse;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemsResponse;
import de.nevini.modules.warframe.api.wfm.model.orders.WfmOrdersResponse;
import de.nevini.modules.warframe.api.wfm.model.statistics.WfmStatisticsResponse;
import de.nevini.modules.warframe.api.wfm.requests.*;
import de.nevini.util.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class WarframeMarketApi {

    private final TokenBucket rateLimit = new TokenBucket(3, 3, 3, TimeUnit.SECONDS);

    @NonNull
    private final OkHttpClient httpClient;

    private @NonNull <T> ApiResponse<T> call(@NonNull WfmApiRequest<T> apiRequest) {
        log.debug("Request: {}", apiRequest);
        // check rate limits
        if (rateLimit.requestToken()) {
            // prepare the GET request
            Request request = apiRequest.getRequest(new Request.Builder()
                    .url(apiRequest.getEndpoint())
                    .addHeader("Platform", "pc")
                    .addHeader("Language", "en")
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

    public @NonNull ApiResponse<WfmItemsResponse> getItems(@NonNull WfmItemsRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<WfmItemResponse> getItem(@NonNull WfmItemRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<WfmOrdersResponse> getOrders(@NonNull WfmOrdersRequest request) {
        return call(request);
    }

    public @NonNull ApiResponse<WfmStatisticsResponse> getStatistics(@NonNull WfmStatisticsRequest request) {
        return call(request);
    }

}
