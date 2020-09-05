package de.nevini.modules.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.osu.api.OsuApi;
import de.nevini.modules.osu.api.OsuApiProvider;
import de.nevini.modules.osu.api.requests.OsuApiGetScoresRequest;
import de.nevini.modules.osu.data.OsuScoreData;
import de.nevini.modules.osu.data.OsuScoreId;
import de.nevini.modules.osu.data.OsuScoreRepository;
import de.nevini.modules.osu.mappers.OsuScoreMapper;
import de.nevini.modules.osu.model.OsuScore;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuScoreService {

    private final Cache<OsuApiGetScoresRequest, List<OsuScore>> requestCache;
    private final Cache<OsuScoreId, OsuScore> cache;
    private final OsuApi api;
    private final OsuScoreRepository repository;
    private final OsuAsyncService asyncService;

    public OsuScoreService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuScoreRepository repository,
            @Autowired OsuAsyncService asyncService
    ) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
        this.repository = repository;
        this.asyncService = asyncService;
    }

    public @NonNull ApiResponse<List<OsuScore>> get(@NonNull OsuApiGetScoresRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<List<OsuScore>> getFromCache(@NonNull OsuApiGetScoresRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<List<OsuScore>> getFromApi(@NonNull OsuApiGetScoresRequest request) {
        ApiResponse<List<OsuScoreData>> response = api.getScores(request).map(list ->
                list.stream().map(score -> OsuScoreMapper.map(score, request)).collect(Collectors.toList())
        );
        ApiResponse<List<OsuScore>> result = response.map(list ->
                list.stream().map(OsuScoreMapper::map).collect(Collectors.toList())
        );
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
            if (request.getUser() == null && request.getUserType() == null && request.getLimit().equals(1)) {
                OsuScoreData scoreData = response.getResult().get(0);
                log.debug("Save data: {}", scoreData);
                repository.save(scoreData);
            }
        }
        return result;
    }

    public @NonNull ApiResponse<OsuScore> get(int beatmapId, int mode, int mods) {
        OsuScoreId scoreId = new OsuScoreId(beatmapId, mode, mods);
        return getFromCache(scoreId).orElse(() ->
                getFromApi(scoreId).orElse(apiResponse ->
                        getFromDatabase(scoreId).orElse(apiResponse)
                )
        );
    }

    public @NonNull ApiResponse<OsuScore> getCached(int beatmapId, int mode, int mods) {
        OsuScoreId scoreId = new OsuScoreId(beatmapId, mode, mods);
        return getFromCache(scoreId).orElse(() ->
                getFromDatabase(scoreId).orElse(() ->
                        getFromApi(scoreId)
                )
        );
    }

    private @NonNull ApiResponse<OsuScore> getFromCache(@NonNull OsuScoreId scoreId) {
        return ApiResponse.ok(cache.getIfPresent(scoreId));
    }

    private @NonNull ApiResponse<OsuScore> getFromApi(@NonNull OsuScoreId scoreId) {
        OsuApiGetScoresRequest request = OsuApiGetScoresRequest.builder()
                .beatmapId(scoreId.getBeatmapId())
                .mode(scoreId.getMode())
                .mods(scoreId.getMods())
                .limit(1)
                .build();
        return get(request).map(list -> list.stream().findFirst().orElse(null));
    }

    private @NonNull ApiResponse<OsuScore> getFromDatabase(@NonNull OsuScoreId scoreId) {
        // queue update
        OsuApiGetScoresRequest request = OsuApiGetScoresRequest.builder()
                .beatmapId(scoreId.getBeatmapId())
                .mode(scoreId.getMode())
                .mods(scoreId.getMods())
                .limit(1)
                .build();
        asyncService.addTask(request, () -> get(request));
        // get from database
        return ApiResponse.ok(repository.findById(scoreId).map(OsuScoreMapper::map).orElse(null));
    }

}
