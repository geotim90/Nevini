package de.nevini.modules.warframe.cache.wfm;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.core.api.ApiResponse;
import de.nevini.modules.warframe.api.wfm.WarframeMarketApi;
import de.nevini.modules.warframe.api.wfm.model.orders.WfmOrder;
import de.nevini.modules.warframe.api.wfm.model.orders.WfmOrdersResponse;
import de.nevini.modules.warframe.api.wfm.requests.WfmOrdersRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class WfmOrderDataService {

    private final Cache<String, Collection<WfmOrder>> readCache;
    private final WarframeMarketApi api;

    public WfmOrderDataService(@Autowired WarframeMarketApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .maximumSize(300)
                .build();
        this.api = apiProvider.getApi();
    }

    public Collection<WfmOrder> get(@NonNull String itemUrlName) {
        log.trace("get({})", itemUrlName);
        return getFromReadCache(itemUrlName).orElseGet(() ->
                getFromApi(itemUrlName).orElse(null)
        );
    }

    private @NonNull Optional<Collection<WfmOrder>> getFromReadCache(@NonNull String itemUrlName) {
        log.trace("getFromReadCache({})", itemUrlName);
        return Optional.ofNullable(readCache.getIfPresent(itemUrlName));
    }

    private @NonNull Optional<Collection<WfmOrder>> getFromApi(@NonNull String itemUrlName) {
        log.trace("getFromApi({})", itemUrlName);
        ApiResponse<WfmOrdersResponse> response = api.getOrders(WfmOrdersRequest.builder()
                .itemUrlName(itemUrlName).build());
        WfmOrdersResponse result = response.getResult();
        if (result != null && result.getPayload() != null && result.getPayload().getOrders() != null) {
            return Optional.of(cache(itemUrlName, result.getPayload().getOrders()));
        }
        return Optional.empty();
    }

    private @NonNull Collection<WfmOrder> cache(@NonNull String itemUrlName, @NonNull Collection<WfmOrder> payload) {
        log.debug("Cache data: {}", payload);
        readCache.put(itemUrlName, payload);
        return payload;
    }

}
