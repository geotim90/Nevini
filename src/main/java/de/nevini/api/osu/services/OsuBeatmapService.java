package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetBeatmapsRequest;
import de.nevini.api.osu.jpa.beatmap.OsuBeatmapData;
import de.nevini.api.osu.jpa.beatmap.OsuBeatmapRepository;
import de.nevini.api.osu.mappers.OsuBeatmapMapper;
import de.nevini.api.osu.model.OsuBeatmap;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuBeatmapService {

    private final Cache<OsuApiGetBeatmapsRequest, List<OsuBeatmap>> requestCache;
    private final Cache<Integer, OsuBeatmap> cache;
    private final OsuApi api;
    private final OsuBeatmapRepository repository;

    public OsuBeatmapService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuBeatmapRepository repository
    ) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
        this.repository = repository;
    }

    public @NonNull ApiResponse<List<OsuBeatmap>> get(@NonNull OsuApiGetBeatmapsRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<List<OsuBeatmap>> getFromCache(@NonNull OsuApiGetBeatmapsRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<List<OsuBeatmap>> getFromApi(@NonNull OsuApiGetBeatmapsRequest request) {
        ApiResponse<List<OsuBeatmapData>> response = api.getBeatmaps(request).map(list ->
                list.stream().map(OsuBeatmapMapper::map).collect(Collectors.toList())
        );
        ApiResponse<List<OsuBeatmap>> result = response.map(list ->
                list.stream().map(OsuBeatmapMapper::map).collect(Collectors.toList())
        );
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
            if (request.getMode() == null && request.getMods() == null) {
                result.getResult().forEach(beatmap -> cache.put(beatmap.getBeatmapId(), beatmap));
                repository.saveAll(response.getResult());
            }
        }
        return result;
    }

    public @NonNull ApiResponse<OsuBeatmap> get(int beatmapId) {
        return getFromCache(beatmapId).orElse(() ->
                getFromApi(beatmapId).orElse(apiResponse ->
                        getFromDatabase(beatmapId).orElse(apiResponse)
                )
        );
    }

    public @NonNull ApiResponse<OsuBeatmap> getCached(int beatmapId) {
        return getFromCache(beatmapId).orElse(() ->
                getFromDatabase(beatmapId).orElse(() ->
                        getFromApi(beatmapId)
                )
        );
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromCache(int beatmapId) {
        return ApiResponse.ok(cache.getIfPresent(beatmapId));
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromApi(int beatmapId) {
        OsuApiGetBeatmapsRequest request = OsuApiGetBeatmapsRequest.builder()
                .beatmapId(beatmapId)
                .build();
        return get(request).map(list -> list.stream().findFirst().orElse(null));
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromDatabase(int beatmapId) {
        return ApiResponse.ok(repository.findById(beatmapId).map(OsuBeatmapMapper::map).orElse(null));
    }

    public Collection<OsuBeatmap> find(@NonNull String query) {
        return repository.findAllByTitleContainsIgnoreCase(query)
                .stream().map(OsuBeatmapMapper::map).collect(Collectors.toList());
    }

}
