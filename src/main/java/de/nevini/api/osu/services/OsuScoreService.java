package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetScoresRequest;
import de.nevini.api.osu.jpa.score.OsuScoreData;
import de.nevini.api.osu.jpa.score.OsuScoreId;
import de.nevini.api.osu.jpa.score.OsuScoreRepository;
import de.nevini.api.osu.mappers.OsuScoreMapper;
import de.nevini.api.osu.model.OsuScore;
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

    public OsuScoreService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuScoreRepository repository
    ) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
        this.repository = repository;
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
                .build();
        ApiResponse<List<OsuScoreData>> response = api.getScores(request).map(list ->
                list.stream().map(score -> OsuScoreMapper.map(score, request)).collect(Collectors.toList())
        );
        ApiResponse<List<OsuScore>> result = response.map(list ->
                list.stream().map(OsuScoreMapper::map).collect(Collectors.toList())
        );
        if (!result.isEmpty() && result.getResult().size() == 1) {
            OsuScore score = result.getResult().get(0);
            cache.put(scoreId, score);
            repository.save(response.getResult().get(0));
            return ApiResponse.ok(score);
        }
        return result.map(list -> list.stream().findFirst().orElse(null));
    }

    private @NonNull ApiResponse<OsuScore> getFromDatabase(@NonNull OsuScoreId scoreId) {
        return ApiResponse.ok(repository.findById(scoreId).map(OsuScoreMapper::map).orElse(null));
    }

}
