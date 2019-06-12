package de.nevini.api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.NonNull;

import java.time.Duration;
import java.util.Collection;

public class ApiBackedCache<T> {

    private final Api api;
    private final Cache<ApiRequest<T>, ApiResponse<T>> cache;

    public ApiBackedCache(@NonNull Api api, @NonNull Duration expireAfterAccessDuration, int limit) {
        this.api = api;
        cache = CacheBuilder.newBuilder().expireAfterAccess(expireAfterAccessDuration).maximumSize(limit).build();
    }

    /**
     * Calls the API and updates the cache if the call was successful.
     *
     * @param request the {@link ApiRequest}
     * @return the {@link ApiResponse} of the API call
     */
    public @NonNull ApiResponse<T> call(@NonNull ApiRequest<T> request) {
        ApiResponse<T> response = api.call(request);
        // update cache
        if (response.isOk()) {
            cache.put(request, response);
        }
        return response;
    }

    /**
     * Calls the API and updates the cache if the call was successful.
     * Queries the cache if the call was rate limited.
     *
     * @param request the {@link ApiRequest}
     * @return the {@link ApiResponse} of the API call or cache
     */
    public @NonNull ApiResponse<T> callOrGet(@NonNull ApiRequest<T> request) {
        ApiResponse<T> apiResponse = call(request);

        // get from cache if rate limited
        if (apiResponse.isRateLimited()) {
            ApiResponse<T> cachedResponse = get(request);
            if (cachedResponse == null) {
                // return rate limited response if no cached response was present
                return apiResponse;
            } else {
                // return cached response if present
                return cachedResponse;
            }
        }

        return apiResponse;
    }

    /**
     * Queries the cache.
     *
     * @param request the {@link ApiRequest}
     * @return the {@link ApiResponse} of the cache or {@code null}
     */
    public ApiResponse<T> get(@NonNull ApiRequest<T> request) {
        return cache.getIfPresent(request);
    }

    /**
     * Queries the cache or calls the API if no cached response is available.
     *
     * @param request the {@link ApiRequest}
     * @return the {@link ApiResponse} of the cache or API call
     */
    public @NonNull ApiResponse<T> getOrCall(@NonNull ApiRequest<T> request) {
        ApiResponse<T> cachedResponse = get(request);
        if (cachedResponse == null) {
            return call(request);
        } else {
            return cachedResponse;
        }
    }

    /**
     * Returns all cached responses.
     */
    public Collection<ApiResponse<T>> getAll() {
        return cache.asMap().values();
    }

}
