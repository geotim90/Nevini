package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetReplayRequest;
import de.nevini.api.osu.mappers.OsuReplayMapper;
import de.nevini.api.osu.model.OsuReplay;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class OsuReplayService {

    private final Cache<OsuApiGetReplayRequest, OsuReplay> requestCache;
    private final OsuApi api;

    public OsuReplayService(@Autowired OsuApiProvider apiProvider) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
    }

    public @NonNull ApiResponse<OsuReplay> get(@NonNull OsuApiGetReplayRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<OsuReplay> getFromCache(@NonNull OsuApiGetReplayRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<OsuReplay> getFromApi(@NonNull OsuApiGetReplayRequest request) {
        ApiResponse<OsuReplay> result = api.getReplay(request).map(replay -> OsuReplayMapper.map(replay, request));
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
        }
        return result;
    }

}
