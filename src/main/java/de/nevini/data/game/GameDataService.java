package de.nevini.data.game;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalNotification;
import de.nevini.jpa.game.GameData;
import de.nevini.jpa.game.GameRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GameDataService {

    private final Cache<Long, GameData> writeCache;
    private final Cache<Long, GameData> readCache;
    private final GameRepository repository;

    public GameDataService(@Autowired GameRepository repository) {
        // non-volatile data; only discard if no longer in use -> expireAfterAccess
        this.writeCache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(1))
                .maximumSize(1000)
                .removalListener(this::save)
                .build();
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(1))
                .maximumSize(1000)
                .build();
        this.repository = repository;
    }

    public GameData get(long id) {
        log.trace("get({})", id);
        return getFromWriteCache(id).orElseGet(() ->
                getFromReadCache(id).orElseGet(() ->
                        getFromDatabase(id).orElse(null)
                )
        );
    }

    private @NonNull Optional<GameData> getFromWriteCache(long id) {
        log.trace("getFromWriteCache({})", id);
        return Optional.ofNullable(writeCache.getIfPresent(id));
    }

    private @NonNull Optional<GameData> getFromReadCache(long id) {
        log.trace("getFromReadCache({})", id);
        return Optional.ofNullable(readCache.getIfPresent(id));
    }

    private @NonNull Optional<GameData> getFromDatabase(long id) {
        log.trace("getFromDatabase({})", id);
        return repository.findById(id).map(this::cache);
    }

    private @NonNull GameData cache(@NonNull GameData data) {
        log.debug("Cache data: {}", data);
        readCache.put(data.getId(), data);
        return data;
    }

    public void put(@NonNull GameData data) {
        log.debug("Put data: {}", data);
        writeCache.put(data.getId(), data);
    }

    private void save(@NonNull RemovalNotification<Long, GameData> e) {
        log.trace("save({})", e);
        GameData data = e.getValue();
        if (data != null && e.getCause() != RemovalCause.REPLACED) {
            log.debug("Save data: {}", data);
            repository.save(data);
        }
    }

    @PreDestroy
    public void flushWriteCache() {
        log.trace("flushWriteCache()");
        // force all cache entries to expire so that they can be written to the database
        writeCache.invalidateAll();
    }

    public @NonNull Collection<GameData> findAllByNameContainsIgnoreCase(@NonNull String query) {
        flushWriteCache();
        return repository.findAllByNameContainsIgnoreCase(query);
    }

}
