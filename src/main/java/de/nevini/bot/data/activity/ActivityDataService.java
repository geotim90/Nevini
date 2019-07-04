package de.nevini.bot.data.activity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalNotification;
import de.nevini.bot.db.activity.ActivityData;
import de.nevini.bot.db.activity.ActivityId;
import de.nevini.bot.db.activity.ActivityRepository;
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
public class ActivityDataService {

    private final Cache<ActivityId, ActivityData> writeCache;
    private final Cache<ActivityId, ActivityData> readCache;
    private final ActivityRepository repository;

    public ActivityDataService(@Autowired ActivityRepository repository) {
        this.writeCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1000)
                .removalListener(this::save)
                .build();
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(1000)
                .build();
        this.repository = repository;
    }

    private @NonNull ActivityId getKey(@NonNull ActivityData data) {
        return new ActivityId(data.getUser(), data.getType(), data.getId(), data.getSource());
    }

    public ActivityData get(@NonNull ActivityId id) {
        log.trace("get({})", id);
        return getFromWriteCache(id).orElseGet(() ->
                getFromReadCache(id).orElseGet(() ->
                        getFromDatabase(id).orElse(null)
                )
        );
    }

    private @NonNull Optional<ActivityData> getFromWriteCache(@NonNull ActivityId id) {
        log.trace("getFromWriteCache({})", id);
        return Optional.ofNullable(writeCache.getIfPresent(id));
    }

    private @NonNull Optional<ActivityData> getFromReadCache(@NonNull ActivityId id) {
        log.trace("getFromReadCache({})", id);
        return Optional.ofNullable(readCache.getIfPresent(id));
    }

    private @NonNull Optional<ActivityData> getFromDatabase(@NonNull ActivityId id) {
        log.trace("getFromDatabase({})", id);
        return repository.findById(id).map(this::cache);
    }

    private @NonNull ActivityData cache(@NonNull ActivityData data) {
        log.debug("Cache data: {}", data);
        readCache.put(getKey(data), data);
        return data;
    }

    public void put(@NonNull ActivityData data) {
        log.debug("Put data: {}", data);
        writeCache.put(getKey(data), data);
    }

    private void save(@NonNull RemovalNotification<ActivityId, ActivityData> e) {
        log.trace("save({})", e);
        ActivityData data = e.getValue();
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

    public Collection<ActivityData> findAllByUserAndTypeAndSourceIn(long user, byte type, long[] source) {
        flushWriteCache();
        return repository.findAllByUserAndTypeAndSourceIn(user, type, source);
    }

    public Collection<ActivityData> findAllByTypeAndIdAndSourceIn(byte type, long id, long[] source) {
        flushWriteCache();
        return repository.findAllByTypeAndIdAndSourceIn(type, id, source);
    }

}
