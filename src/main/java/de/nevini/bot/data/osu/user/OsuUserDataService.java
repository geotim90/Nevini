package de.nevini.bot.data.osu.user;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.osu.OsuApi;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
import de.nevini.api.osu.model.OsuUserType;
import de.nevini.api.osu.requests.OsuUserRequest;
import de.nevini.bot.data.osu.OsuApiProvider;
import de.nevini.bot.db.osu.user.OsuUserData;
import de.nevini.bot.db.osu.user.OsuUserId;
import de.nevini.bot.db.osu.user.OsuUserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OsuUserDataService {

    private final Cache<Integer, OsuUser> readCacheById;
    private final Cache<Pair<String, OsuMode>, OsuUser> readCacheByNameAndMode;
    private final OsuApi api;
    private final OsuUserRepository repository;

    public OsuUserDataService(
            @Autowired OsuApiProvider apiProvider,
            @Autowired OsuUserRepository repository
    ) {
        this.readCacheById = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1200)
                .build();
        this.readCacheByNameAndMode = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1200)
                .build();
        this.api = apiProvider.getApi();
        this.repository = repository;
    }

    public OsuUser get(int userId) {
        log.trace("get({})", userId);
        return getFromReadCache(userId).orElseGet(() ->
                getFromApi(userId).orElseGet(() ->
                        getFromDatabase(userId).orElse(null)
                )
        );
    }

    public OsuUser get(@NonNull String userName, OsuMode mode) {
        Pair<String, OsuMode> args = Pair.of(userName, ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD));
        log.trace("get({})", args);
        return getFromReadCache(args).orElseGet(() ->
                getFromApi(args).orElseGet(() ->
                        getFromDatabase(args).orElse(null)
                )
        );
    }

    public OsuUser getCached(int userId) {
        log.trace("getCached({})", userId);
        return getFromReadCache(userId).orElseGet(() ->
                getFromDatabase(userId).orElseGet(() ->
                        getFromApi(userId).orElse(null)
                )
        );
    }

    private @NonNull Optional<OsuUser> getFromReadCache(int userId) {
        log.trace("getFromReadCache({})", userId);
        return Optional.ofNullable(readCacheById.getIfPresent(userId));
    }

    private @NonNull Optional<OsuUser> getFromReadCache(@NonNull Pair<String, OsuMode> args) {
        log.trace("getFromReachCache({})", args);
        return Optional.ofNullable(readCacheByNameAndMode.getIfPresent(args));
    }

    private @NonNull Optional<OsuUser> getFromApi(int userId) {
        log.trace("getFromApi({})", userId);
        ApiResponse<List<OsuUser>> response = api.getUser(
                OsuUserRequest.builder()
                        .user(Integer.toString(userId))
                        .userType(OsuUserType.ID)
                        .mode(OsuMode.STANDARD)
                        .build()
        );
        List<OsuUser> results = response.getResult();
        if (results != null && results.size() == 1) {
            OsuUser beatmap = results.get(0);
            log.debug("Save data: {}", beatmap);
            repository.save(OsuUserDataMapper.convert(beatmap, OsuMode.STANDARD));
            return Optional.of(cache(beatmap, OsuMode.STANDARD));
        }
        return Optional.empty();
    }

    private @NonNull Optional<OsuUser> getFromApi(@NonNull Pair<String, OsuMode> args) {
        log.trace("getFromApi({})", args);
        ApiResponse<List<OsuUser>> response = api.getUser(
                OsuUserRequest.builder()
                        .user(args.getLeft())
                        .mode(args.getRight())
                        .build()
        );
        List<OsuUser> results = response.getResult();
        if (results != null && results.size() == 1) {
            OsuUser beatmap = results.get(0);
            log.debug("Save data: {}", beatmap);
            repository.save(OsuUserDataMapper.convert(beatmap, args.getRight()));
            return Optional.of(cache(beatmap, args.getRight()));
        }
        return Optional.empty();
    }

    private @NonNull Optional<OsuUser> getFromDatabase(int userId) {
        log.trace("getFromDatabase({})", userId);
        Optional<OsuUserData> data = repository.findById(new OsuUserId(userId, OsuMode.STANDARD.getId()));
        return data.map(OsuUserData -> cache(OsuUserDataMapper.convert(OsuUserData), OsuMode.STANDARD));
    }

    private @NonNull Optional<OsuUser> getFromDatabase(@NonNull Pair<String, OsuMode> args) {
        log.trace("getFromDatabase({})", args);
        Optional<OsuUserData> data = repository.findByUserNameAndMode(args.getLeft(), args.getRight().getId());
        return data.map(OsuUserData -> cache(OsuUserDataMapper.convert(OsuUserData), args.getRight()));
    }

    private @NonNull OsuUser cache(@NonNull OsuUser user, @NonNull OsuMode mode) {
        log.debug("Cache data: {}", user);
        if (mode == OsuMode.STANDARD) {
            readCacheById.put(user.getUserId(), user);
        }
        readCacheByNameAndMode.put(Pair.of(user.getUserName(), mode), user);
        return user;
    }

}
