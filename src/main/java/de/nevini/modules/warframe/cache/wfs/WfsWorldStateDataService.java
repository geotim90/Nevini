package de.nevini.modules.warframe.cache.wfs;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfs.WarframeStatusApi;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.requests.WfsWorldStateRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WfsWorldStateDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, WfsWorldState> readCache;
    private final WarframeStatusApi api;
    private final Map<String, WfsWorldState> backup;

    public WfsWorldStateDataService(@Autowired WarframeStatusApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(15))
                .build();
        this.api = apiProvider.getApi();
        this.backup = new ConcurrentHashMap<>();
    }

    public WfsWorldState get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(getFromBackup())
        );
    }

    private @NonNull Optional<WfsWorldState> getFromReadCache() {
        log.trace("getFromReadCache()");
        return Optional.ofNullable(readCache.getIfPresent(KEY));
    }

    private @NonNull Optional<WfsWorldState> getFromApi() {
        log.trace("getFromApi()");
        ApiResponse<WfsWorldState> response = api.getWorldState(WfsWorldStateRequest.builder().build());
        WfsWorldState result = response.getResult();
        if (result != null) {
            return Optional.of(cache(result));
        }
        return Optional.empty();
    }

    private @NonNull WfsWorldState cache(@NonNull WfsWorldState names) {
        log.debug("Cache data: {}", names);
        readCache.put(KEY, names);
        backup.put(KEY, names);
        return names;
    }

    private WfsWorldState getFromBackup() {
        log.trace("getFromBackup()");
        return backup.get(KEY);
    }

}
