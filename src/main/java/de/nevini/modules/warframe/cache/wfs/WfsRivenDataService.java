package de.nevini.modules.warframe.cache.wfs;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.WarframeStatusApi;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRiven;
import de.nevini.modules.warframe.api.wfs.requests.WfsRivensRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WfsRivenDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, Collection<WfsRiven>> readCache;
    private final WarframeStatusApi api;
    private final Map<String, Collection<WfsRiven>> backup;

    public WfsRivenDataService(@Autowired WarframeStatusApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(15))
                .build();
        this.api = apiProvider.getApi();
        this.backup = new ConcurrentHashMap<>();
    }

    public Collection<WfsRiven> get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(getFromBackup())
        );
    }

    private @NonNull Optional<Collection<WfsRiven>> getFromReadCache() {
        log.trace("getFromReadCache()");
        return Optional.ofNullable(readCache.getIfPresent(KEY));
    }

    private @NonNull Optional<Collection<WfsRiven>> getFromApi() {
        log.trace("getFromApi()");
        ApiResponse<Map<String, Map<String, WfsRiven>>> response = api.getRivens(WfsRivensRequest.builder().build());
        Map<String, Map<String, WfsRiven>> map = response.getResult();
        if (map != null && map.size() > 0) {
            Collection<WfsRiven> collection = new ArrayList<>();
            for (Map<String, WfsRiven> subMap : map.values()) {
                collection.addAll(subMap.values());
            }
            return Optional.of(cache(collection));
        }
        return Optional.empty();
    }

    private @NonNull Collection<WfsRiven> cache(@NonNull Collection<WfsRiven> names) {
        log.debug("Cache data: {}", names);
        readCache.put(KEY, names);
        backup.put(KEY, names);
        return names;
    }

    private Collection<WfsRiven> getFromBackup() {
        log.trace("getFromBackup()");
        return backup.get(KEY);
    }

}
