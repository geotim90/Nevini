package de.nevini.bot.data.warframe.wfm;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.WarframeMarketApi;
import de.nevini.api.wfm.model.items.WfmItemName;
import de.nevini.api.wfm.model.items.WfmItemsResponse;
import de.nevini.api.wfm.requests.WfmItemsRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class WfmItemNameDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, Collection<WfmItemName>> readCache;
    private final WarframeMarketApi api;

    public WfmItemNameDataService(@Autowired WarframeMarketApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))
                .build();
        this.api = apiProvider.getApi();
    }

    public Collection<WfmItemName> get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(null)
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
        return names;
    }

}
