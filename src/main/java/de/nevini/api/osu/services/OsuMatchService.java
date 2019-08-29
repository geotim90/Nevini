package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetMatchRequest;
import de.nevini.api.osu.mappers.OsuMatchMapper;
import de.nevini.api.osu.model.OsuMatch;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class OsuMatchService {

    private final Cache<OsuApiGetMatchRequest, OsuMatch> requestCache;
    private final OsuApi api;

    public OsuMatchService(@Autowired OsuApiProvider apiProvider) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
    }

    public @NonNull ApiResponse<OsuMatch> get(@NonNull OsuApiGetMatchRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<OsuMatch> getFromCache(@NonNull OsuApiGetMatchRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<OsuMatch> getFromApi(@NonNull OsuApiGetMatchRequest request) {
        ApiResponse<OsuMatch> result = api.getMatch(request).map(OsuMatchMapper::map);
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
        }
        return result;
    }

}
