package de.nevini.api.wfm;

import de.nevini.api.Api;
import de.nevini.api.ApiRequest;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.model.item.WfmItemResponse;
import de.nevini.api.wfm.model.items.WfmItemsResponse;
import de.nevini.api.wfm.model.orders.WfmOrdersResponse;
import de.nevini.api.wfm.model.statistics.WfmStatisticsResponse;
import de.nevini.api.wfm.requests.WfmItemRequest;
import de.nevini.api.wfm.requests.WfmItemsRequest;
import de.nevini.api.wfm.requests.WfmOrdersRequest;
import de.nevini.api.wfm.requests.WfmStatisticsRequest;
import de.nevini.commons.concurrent.TokenBucket;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class WarframeMarketApi implements Api {

    // rate limits according to KycKyc
    private final TokenBucket rateLimit = new TokenBucket(3, 3, 5, TimeUnit.SECONDS);

    @NonNull
    private final OkHttpClient httpClient;

    @Override
    public @NonNull <T> ApiResponse<T> call(@NonNull ApiRequest<T> apiRequest) {
        // check rate limits
        if (rateLimit.requestToken()) {
            // prepare the GET request
            Request request = apiRequest.getRequest(new Request.Builder()
                    .url(apiRequest.getEndpoint())
                    .addHeader("Platform", "PC")
                    .addHeader("Language", "en")
                    .get());
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
