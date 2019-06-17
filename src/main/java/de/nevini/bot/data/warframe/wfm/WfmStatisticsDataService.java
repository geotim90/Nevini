package de.nevini.bot.data.warframe.wfm;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfm.WarframeMarketApi;
import de.nevini.api.wfm.model.statistics.WfmStatisticsPayload;
import de.nevini.api.wfm.model.statistics.WfmStatisticsResponse;
import de.nevini.api.wfm.requests.WfmStatisticsRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
public class WfmStatisticsDataService {

    private final Cache<String, WfmStatisticsPayload> readCache;
    private final WarframeMarketApi api;

    public WfmStatisticsDataService(@Autowired WarframeMarketApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .maximumSize(300)
                .build();
        this.api = apiProvider.getApi();
    }

    public WfmStatisticsPayload get(@NonNull String itemUrlName) {
        log.trace("get({})", itemUrlName);
        return getFromReadCache(itemUrlName).orElseGet(() ->
                getFromApi(itemUrlName).orElse(null)
        );
    }

    private @NonNull Optional<WfmStatisticsPayload> getFromReadCache(@NonNull String itemUrlName) {
        log.trace("getFromReadCache({})", itemUrlName);
        return Optional.ofNullable(readCache.getIfPresent(itemUrlName));
    }

    private @NonNull Optional<WfmStatisticsPayload> getFromApi(@NonNull String itemUrlName) {
        log.trace("getFromApi({})", itemUrlName);
        ApiResponse<WfmStatisticsResponse> response = api.getStatistics(WfmStatisticsRequest.builder()
                .itemUrlName(itemUrlName).build());
        WfmStatisticsResponse result = response.getResult();
        if (result != null && result.getPayload() != null) {
            return Optional.of(cache(itemUrlName, result.getPayload()));
        }
        return Optional.empty();
    }

    private @NonNull WfmStatisticsPayload cache(@NonNull String itemUrlName, @NonNull WfmStatisticsPayload payload) {
        log.debug("Cache data: {}", payload);
        readCache.put(itemUrlName, payload);
        return payload;
    }

}
