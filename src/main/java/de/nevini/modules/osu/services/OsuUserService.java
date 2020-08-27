package de.nevini.modules.osu.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.osu.api.OsuApi;
import de.nevini.modules.osu.api.OsuApiProvider;
import de.nevini.modules.osu.api.requests.OsuApiGetUserRequest;
import de.nevini.modules.osu.api.requests.OsuUserType;
import de.nevini.modules.osu.data.OsuUserData;
import de.nevini.modules.osu.data.OsuUserId;
import de.nevini.modules.osu.data.OsuUserRepository;
import de.nevini.modules.osu.mappers.OsuUserMapper;
import de.nevini.modules.osu.model.OsuMode;
import de.nevini.modules.osu.model.OsuUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OsuUserService {

    private final Cache<OsuApiGetUserRequest, List<OsuUser>> requestCache;
    private final Cache<OsuUserId, OsuUser> cache;
    private final OsuApi api;
    private final OsuUserRepository repository;
    private final OsuAsyncService asyncService;

    public OsuUserService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuUserRepository repository,
            @Autowired OsuAsyncService asyncService
    ) {
        this.requestCache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();
        this.api = apiProvider.getApi();
        this.repository = repository;
        this.asyncService = asyncService;
    }

    public @NonNull ApiResponse<List<OsuUser>> get(@NonNull OsuApiGetUserRequest request) {
        return getFromCache(request).orElse(() ->
                getFromApi(request)
        );
    }

    private @NonNull ApiResponse<List<OsuUser>> getFromCache(@NonNull OsuApiGetUserRequest request) {
        return ApiResponse.ok(requestCache.getIfPresent(request));
    }

    private @NonNull ApiResponse<List<OsuUser>> getFromApi(@NonNull OsuApiGetUserRequest request) {
        ApiResponse<List<OsuUserData>> response = api.getUser(request).map(list ->
                list.stream().map(user -> OsuUserMapper.map(user, request)).collect(Collectors.toList())
        );
        ApiResponse<List<OsuUser>> result = response.map(list ->
                list.stream().map(OsuUserMapper::map).collect(Collectors.toList())
        );
        if (!result.isEmpty()) {
            requestCache.put(request, result.getResult());
            result.getResult().forEach(user -> cache.put(new OsuUserId(user.getUserId(),
                    ObjectUtils.defaultIfNull(request.getMode(), OsuMode.STANDARD.getId())), user));
            log.debug("Save data: {}", response.getResult());
            try {
                repository.saveAll(response.getResult());
            } catch (DataAccessException e) {
                // new users may have one or more null values
                log.debug("Save data failed", e);
                return ApiResponse.error(e);
            }
        }
        return result;
    }

    public ApiResponse<OsuUser> get(int userId, int mode) {
        OsuUserId id = new OsuUserId(userId, mode);
        return getFromCache(id).orElse(() ->
                getFromApi(id).orElse(apiResponse ->
                        getFromDatabase(id).orElse(apiResponse)
                )
        );
    }

    public ApiResponse<OsuUser> getCached(int userId, int mode) {
        OsuUserId id = new OsuUserId(userId, mode);
        return getFromCache(id).orElse(() ->
                getFromDatabase(id).orElse(() ->
                        getFromApi(id)
                )
        );
    }

    private @NonNull ApiResponse<OsuUser> getFromCache(@NonNull OsuUserId id) {
        return ApiResponse.ok(cache.getIfPresent(id));
    }

    private @NonNull ApiResponse<OsuUser> getFromApi(@NonNull OsuUserId id) {
        OsuApiGetUserRequest request = OsuApiGetUserRequest.builder()
                .user(Integer.toString(id.getUserId()))
                .userType(OsuUserType.ID)
                .mode(id.getMode())
                .build();
        return get(request).map(list -> list.stream().findFirst().orElse(null));
    }

    private @NonNull ApiResponse<OsuUser> getFromDatabase(@NonNull OsuUserId id) {
        // queue update
        OsuApiGetUserRequest request = OsuApiGetUserRequest.builder()
                .user(Integer.toString(id.getUserId()))
                .userType(OsuUserType.ID)
                .mode(id.getMode())
                .build();
        asyncService.addTask(request, () -> get(request));
        // get from database
        return ApiResponse.ok(repository.findById(id).map(OsuUserMapper::map).orElse(null));
    }

}
