package de.nevini.api.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.external.OsuApi;
import de.nevini.api.osu.external.OsuApiProvider;
import de.nevini.api.osu.external.requests.OsuApiGetBeatmapsRequest;
import de.nevini.api.osu.jpa.beatmap.*;
import de.nevini.api.osu.mappers.OsuBeatmapMapper;
import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.api.osu.model.OsuMod;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuBeatmapService {

    private final Cache<OsuApiGetBeatmapsRequest, List<OsuBeatmap>> requestCache;
    private final Cache<Integer, OsuBeatmap> cache;
    private final OsuApi api;
    private final OsuBeatmapRepository repository;
    private final OsuBeatmapsetRepository beatmapsetRepository;
    private final OsuBeatmapDifficultyRepository difficultyRepository;
    private final OsuScoreService scoreService;
    private final OsuAsyncService asyncService;
    private final OsuPruneService pruneService;

    public OsuBeatmapService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuBeatmapRepository repository,
            @Autowired OsuBeatmapsetRepository beatmapsetRepository,
            @Autowired OsuBeatmapDifficultyRepository difficultyRepository,
            @Autowired OsuScoreService scoreService,
            @Autowired OsuAsyncService asyncService,
            @Autowired OsuPruneService pruneService
    ) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
        this.repository = repository;
        this.beatmapsetRepository = beatmapsetRepository;
        this.difficultyRepository = difficultyRepository;
        this.scoreService = scoreService;
        this.asyncService = asyncService;
        this.pruneService = pruneService;
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
        ApiResponse<List<OsuBeatmapDataWrapper>> response = api.getBeatmaps(request).map(list ->
                list.stream().map(beatmap -> OsuBeatmapMapper.map(beatmap, scoreService.getCached(
                        beatmap.getBeatmapId(),
                        ObjectUtils.defaultIfNull(request.getMode(), beatmap.getMode()),
                        ObjectUtils.defaultIfNull(request.getMods(), OsuMod.NONE)
                        ).getResult())
                ).collect(Collectors.toList())
        );
        ApiResponse<List<OsuBeatmap>> result = response.map(list ->
                list.stream().map(OsuBeatmapMapper::map).collect(Collectors.toList())
        );
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
            result.getResult().forEach(beatmap -> cache.put(beatmap.getBeatmapId(), beatmap));
            log.debug("Save data: {}", response.getResult());
            repository.saveAll(response.getResult().stream().map(OsuBeatmapDataWrapper::getBeatmap).collect(Collectors.toList()));
            beatmapsetRepository.saveAll(response.getResult().stream().map(OsuBeatmapDataWrapper::getBeatmapset).collect(Collectors.toList()));
            difficultyRepository.saveAll(response.getResult().stream().map(OsuBeatmapDataWrapper::getDifficulty).collect(Collectors.toList()));
        } else if (result.isOk() && request.getBeatmapId() != null) {
            pruneService.pruneBeatmap(request.getBeatmapId());
        } else if (result.isOk() && request.getBeatmapsetId() != null) {
            pruneService.pruneBeatmapset(request.getBeatmapsetId());
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
                .limit(1)
                .build();
        return get(request).map(list -> list.stream().findFirst().orElse(null));
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromDatabase(int beatmapId) {
        // queue update
        OsuApiGetBeatmapsRequest request = OsuApiGetBeatmapsRequest.builder()
                .beatmapId(beatmapId)
                .build();
        asyncService.addTask(request, () -> get(request));
        // get from database
        OsuBeatmapData beatmapData = repository.findById(beatmapId).orElse(null);
        if (beatmapData != null) {
            OsuBeatmapsetData beatmapsetData = beatmapsetRepository.findById(beatmapData.getBeatmapsetId()).orElse(null);
            if (beatmapsetData != null) {
                OsuBeatmapDifficultyData difficultyData = difficultyRepository.findById(new OsuBeatmapDifficultyId(
                        beatmapId, beatmapData.getMode(), OsuMod.NONE
                )).orElse(null);
                if (difficultyData != null && difficultyData.getMaxPp() != null) {
                    return ApiResponse.ok(OsuBeatmapMapper.map(
                            new OsuBeatmapDataWrapper(beatmapData, beatmapsetData, difficultyData))
                    );
                }
            }
        }
        // missing data
        return ApiResponse.empty();
    }

    public @NonNull ApiResponse<OsuBeatmap> get(int beatmapId, int mode, int mods) {
        OsuBeatmapDifficultyId id = new OsuBeatmapDifficultyId(beatmapId, mode, mods);
        return getFromCache(id).orElse(() ->
                getFromApi(id).orElse(apiResponse ->
                        getFromDatabase(id).orElse(apiResponse)
                )
        );
    }

    public @NonNull ApiResponse<OsuBeatmap> getCached(int beatmapId, int mode, int mods) {
        OsuBeatmapDifficultyId id = new OsuBeatmapDifficultyId(beatmapId, mode, mods);
        return getFromCache(id).orElse(() ->
                getFromDatabase(id).orElse(() ->
                        getFromApi(id)
                )
        );
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromCache(@NonNull OsuBeatmapDifficultyId id) {
        OsuBeatmap beatmap = cache.getIfPresent(id.getBeatmapId());
        if (beatmap != null && beatmap.getConvertedMode().getId() == id.getMode()
                && OsuMod.sum(beatmap.getMods()) == id.getMods()
        ) {
            return ApiResponse.ok(beatmap);
        } else {
            return ApiResponse.empty();
        }
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromApi(@NonNull OsuBeatmapDifficultyId id) {
        OsuApiGetBeatmapsRequest request = OsuApiGetBeatmapsRequest.builder()
                .beatmapId(id.getBeatmapId())
                .mode(id.getMode())
                .mods(id.getMods())
                .includeConvertedBeatmaps(true)
                .limit(1)
                .build();
        return get(request).map(list -> list.stream().findFirst().orElse(null));
    }

    private @NonNull ApiResponse<OsuBeatmap> getFromDatabase(@NonNull OsuBeatmapDifficultyId id) {
        // queue update
        OsuApiGetBeatmapsRequest request = OsuApiGetBeatmapsRequest.builder()
                .beatmapId(id.getBeatmapId())
                .mode(id.getMode())
                .mods(id.getMods())
                .includeConvertedBeatmaps(true)
                .build();
        asyncService.addTask(request, () -> get(request));
        // get from database
        OsuBeatmapData beatmapData = repository.findById(id.getBeatmapId()).orElse(null);
        if (beatmapData != null) {
            OsuBeatmapsetData beatmapsetData = beatmapsetRepository.findById(beatmapData.getBeatmapsetId()).orElse(null);
            if (beatmapsetData != null) {
                OsuBeatmapDifficultyData difficultyData = difficultyRepository.findById(new OsuBeatmapDifficultyId(
                        id.getBeatmapId(), id.getMode(), id.getMods()
                )).orElse(null);
                if (difficultyData != null && difficultyData.getMaxPp() != null) {
                    return ApiResponse.ok(OsuBeatmapMapper.map(
                            new OsuBeatmapDataWrapper(beatmapData, beatmapsetData, difficultyData))
                    );
                }
            }
        }
        // missing data
        return ApiResponse.empty();
    }

}
