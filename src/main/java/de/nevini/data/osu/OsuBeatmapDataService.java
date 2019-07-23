package de.nevini.data.osu;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.OsuApi;
import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.api.osu.requests.OsuBeatmapsRequest;
import de.nevini.jpa.osu.beatmap.OsuBeatmapData;
import de.nevini.jpa.osu.beatmap.OsuBeatmapRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuBeatmapDataService {

    private final Cache<Integer, OsuBeatmap> readCache;
    private final OsuApi api;
    private final OsuBeatmapRepository repository;

    public OsuBeatmapDataService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuBeatmapRepository repository
    ) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1200)
                .build();
        this.api = apiProvider.getApi();
        this.repository = repository;
    }

    public OsuBeatmap get(int beatmapId) {
        log.trace("get({})", beatmapId);
        return getFromReadCache(beatmapId).orElseGet(() ->
                getFromApi(beatmapId).orElseGet(() ->
                        getFromDatabase(beatmapId).orElse(null)
                )
        );
    }

    public OsuBeatmap getCached(int beatmapId) {
        log.trace("getCached({})", beatmapId);
        return getFromReadCache(beatmapId).orElseGet(() ->
                getFromDatabase(beatmapId).orElseGet(() ->
                        getFromApi(beatmapId).orElse(null)
                )
        );
    }

    private @NonNull Optional<OsuBeatmap> getFromReadCache(int beatmapId) {
        log.trace("getFromReadCache({})", beatmapId);
        return Optional.ofNullable(readCache.getIfPresent(beatmapId));
    }

    private @NonNull Optional<OsuBeatmap> getFromApi(int beatmapId) {
        log.trace("getFromApi({})", beatmapId);
        ApiResponse<List<OsuBeatmap>> response = api.getBeatmaps(
                OsuBeatmapsRequest.builder().beatmapId(beatmapId).build()
        );
        List<OsuBeatmap> results = response.getResult();
        if (results != null && results.size() == 1) {
            OsuBeatmap beatmap = results.get(0);
            log.debug("Save data: {}", beatmap);
            repository.save(OsuBeatmapDataMapper.convert(beatmap));
            return Optional.of(cache(beatmap));
        }
        return Optional.empty();
    }

    private @NonNull Optional<OsuBeatmap> getFromDatabase(int beatmapId) {
        log.trace("getFromDatabase({})", beatmapId);
        Optional<OsuBeatmapData> data = repository.findById(beatmapId);
        return data.map(osuBeatmapData -> cache(OsuBeatmapDataMapper.convert(osuBeatmapData)));
    }

    private @NonNull OsuBeatmap cache(@NonNull OsuBeatmap beatmap) {
        log.debug("Cache data: {}", beatmap);
        readCache.put(beatmap.getBeatmapId(), beatmap);
        return beatmap;
    }

    public @NonNull Collection<OsuBeatmap> findAllByTitleContainsIgnoreCase(@NonNull String title) {
        Collection<OsuBeatmapData> data = repository.findAllByTitleContainsIgnoreCase(title);
        return data.stream().map(OsuBeatmapDataMapper::convert).collect(Collectors.toList());
    }

}
