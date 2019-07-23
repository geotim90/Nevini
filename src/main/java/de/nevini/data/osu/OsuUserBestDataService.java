package de.nevini.data.osu;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.OsuApi;
import de.nevini.api.osu.model.OsuUserBest;
import de.nevini.api.osu.requests.OsuUserBestRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OsuUserBestDataService {

    private final Cache<OsuUserBestRequest, List<OsuUserBest>> readCache;
    private final OsuApi api;

    public OsuUserBestDataService(@Autowired OsuApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1200)
                .build();
        this.api = apiProvider.getApi();
    }

    public List<OsuUserBest> get(@NonNull OsuUserBestRequest request) {
        log.trace("get({})", request);
        return getFromReadCache(request).orElseGet(() ->
                getFromApi(request).orElse(null)
        );
    }

    private @NonNull Optional<List<OsuUserBest>> getFromReadCache(@NonNull OsuUserBestRequest request) {
        log.trace("getFromReadCache({})", request);
        return Optional.ofNullable(readCache.getIfPresent(request));
    }

    private @NonNull Optional<List<OsuUserBest>> getFromApi(@NonNull OsuUserBestRequest request) {
        log.trace("getFromApi({})", request);
        ApiResponse<List<OsuUserBest>> response = api.getUserBest(request);
        List<OsuUserBest> result = response.getResult();
        if (result != null && !result.isEmpty()) {
            return Optional.of(cache(request, result));
        }
        return Optional.empty();
    }

    private @NonNull List<OsuUserBest> cache(@NonNull OsuUserBestRequest request, @NonNull List<OsuUserBest> result) {
        log.debug("Cache data: {}", result);
        readCache.put(request, result);
        return result;
    }

}
