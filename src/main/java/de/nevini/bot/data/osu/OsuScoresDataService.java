package de.nevini.bot.data.osu;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.OsuApi;
import de.nevini.api.osu.model.OsuScore;
import de.nevini.api.osu.requests.OsuScoresRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OsuScoresDataService {

    private final Cache<OsuScoresRequest, List<OsuScore>> readCache;
    private final OsuApi api;

    public OsuScoresDataService(@Autowired OsuApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1200)
                .build();
        this.api = apiProvider.getApi();
    }

    public List<OsuScore> get(@NonNull OsuScoresRequest request) {
        log.trace("get({})", request);
        return getFromReadCache(request).orElseGet(() ->
                getFromApi(request).orElse(null)
        );
    }

    private @NonNull Optional<List<OsuScore>> getFromReadCache(@NonNull OsuScoresRequest request) {
        log.trace("getFromReadCache({})", request);
        return Optional.ofNullable(readCache.getIfPresent(request));
    }

    private @NonNull Optional<List<OsuScore>> getFromApi(@NonNull OsuScoresRequest request) {
        log.trace("getFromApi({})", request);
        ApiResponse<List<OsuScore>> response = api.getScores(request);
        List<OsuScore> result = response.getResult();
        if (result != null && !result.isEmpty()) {
            return Optional.of(cache(request, result));
        }
        return Optional.empty();
    }

    private @NonNull List<OsuScore> cache(@NonNull OsuScoresRequest request, @NonNull List<OsuScore> result) {
        log.debug("Cache data: {}", result);
        readCache.put(request, result);
        return result;
    }

}
