package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetUserBestRequest;
import de.nevini.api.osu.mappers.OsuUserBestMapper;
import de.nevini.api.osu.model.OsuUserBest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuUserBestService {

    private final Cache<OsuApiGetUserBestRequest, List<OsuUserBest>> requestCache;
    private final OsuApi api;

    public OsuUserBestService(@Autowired OsuApiProvider apiProvider) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
    }

    public @NonNull ApiResponse<List<OsuUserBest>> get(@NonNull OsuApiGetUserBestRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<List<OsuUserBest>> getFromCache(@NonNull OsuApiGetUserBestRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<List<OsuUserBest>> getFromApi(@NonNull OsuApiGetUserBestRequest request) {
        ApiResponse<List<OsuUserBest>> result = api.getUserBest(request).map(list ->
                list.stream().map(best -> OsuUserBestMapper.map(best, request)).collect(Collectors.toList())
        );
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
        }
        return result;
    }

}
