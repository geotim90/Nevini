package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetUserRecentRequest;
import de.nevini.api.osu.mappers.OsuUserRecentMapper;
import de.nevini.api.osu.model.OsuUserRecent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuUserRecentService {

    private final Cache<OsuApiGetUserRecentRequest, List<OsuUserRecent>> requestCache;
    private final OsuApi api;

    public OsuUserRecentService(@Autowired OsuApiProvider apiProvider) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
    }

    public @NonNull ApiResponse<List<OsuUserRecent>> get(@NonNull OsuApiGetUserRecentRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<List<OsuUserRecent>> getFromCache(@NonNull OsuApiGetUserRecentRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<List<OsuUserRecent>> getFromApi(@NonNull OsuApiGetUserRecentRequest request) {
        ApiResponse<List<OsuUserRecent>> result = api.getUserRecent(request).map(list ->
                list.stream().map(recent -> OsuUserRecentMapper.map(recent, request)).collect(Collectors.toList())
        );
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
        }
        return result;
    }

}
