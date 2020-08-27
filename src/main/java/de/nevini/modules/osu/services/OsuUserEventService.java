package de.nevini.modules.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.osu.api.OsuApi;
import de.nevini.modules.osu.api.OsuApiProvider;
import de.nevini.modules.osu.api.requests.OsuApiGetUserRequest;
import de.nevini.modules.osu.mappers.OsuUserEventMapper;
import de.nevini.modules.osu.model.OsuUserEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuUserEventService {

    private final Cache<OsuApiGetUserRequest, List<OsuUserEvent>> requestCache;
    private final OsuApi api;

    public OsuUserEventService(@Autowired OsuApiProvider apiProvider) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
    }

    public @NonNull ApiResponse<List<OsuUserEvent>> get(@NonNull OsuApiGetUserRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<List<OsuUserEvent>> getFromCache(@NonNull OsuApiGetUserRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<List<OsuUserEvent>> getFromApi(@NonNull OsuApiGetUserRequest request) {
        ApiResponse<List<OsuUserEvent>> result = api.getUser(request).map(list -> list.stream().map(user ->
                user.getEvents().stream().map(OsuUserEventMapper::map).collect(Collectors.toList())
        ).findFirst().orElse(null));
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
        }
        return result;
    }

}
