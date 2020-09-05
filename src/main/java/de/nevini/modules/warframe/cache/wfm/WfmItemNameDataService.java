package de.nevini.modules.warframe.cache.wfm;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfm.WarframeMarketApi;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemName;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemsResponse;
import de.nevini.modules.warframe.api.wfm.requests.WfmItemsRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WfmItemNameDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, Collection<WfmItemName>> readCache;
    private final WarframeMarketApi api;
    private final Map<String, Collection<WfmItemName>> backup;

    public WfmItemNameDataService(@Autowired WarframeMarketApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(15))
                .build();
        this.api = apiProvider.getApi();
        this.backup = new ConcurrentHashMap<>();
    }

    public Collection<WfmItemName> get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(getFromBackup())
        );
    }

    private @NonNull Optional<Collection<WfmItemName>> getFromReadCache() {
        log.trace("getFromReadCache()");
        return Optional.ofNullable(readCache.getIfPresent(KEY));
    }

    private @NonNull Optional<Collection<WfmItemName>> getFromApi() {
        log.trace("getFromApi()");
        ApiResponse<WfmItemsResponse> response = api.getItems(WfmItemsRequest.builder().build());
        WfmItemsResponse result = response.getResult();
        if (result != null && result.getPayload() != null && result.getPayload().getItems() != null) {
            return Optional.of(cache(result.getPayload().getItems()));
        }
        return Optional.empty();
    }

    private @NonNull Collection<WfmItemName> cache(@NonNull Collection<WfmItemName> names) {
        log.debug("Cache data: {}", names);
        readCache.put(KEY, names);
        backup.put(KEY, names);
        return names;
    }

    private Collection<WfmItemName> getFromBackup() {
        log.trace("getFromBackup()");
        return backup.get(KEY);
    }

}
