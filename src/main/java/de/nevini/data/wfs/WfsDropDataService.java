package de.nevini.data.wfs;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfs.WarframeStatusApi;
import de.nevini.api.wfs.model.drops.WfsDrop;
import de.nevini.api.wfs.requests.WfsDropsRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WfsDropDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, List<WfsDrop>> readCache;
    private final WarframeStatusApi api;
    private final Map<String, List<WfsDrop>> backup;

    public WfsDropDataService(@Autowired WarframeStatusApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(15))
                .build();
        this.api = apiProvider.getApi();
        this.backup = new ConcurrentHashMap<>();
    }

    public List<WfsDrop> get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(getFromBackup())
        );
    }

    private @NonNull Optional<List<WfsDrop>> getFromReadCache() {
        log.trace("getFromReadCache()");
        return Optional.ofNullable(readCache.getIfPresent(KEY));
    }

    private @NonNull Optional<List<WfsDrop>> getFromApi() {
        log.trace("getFromApi()");
        ApiResponse<List<WfsDrop>> response = api.getDrops(WfsDropsRequest.builder().build());
        List<WfsDrop> result = response.getResult();
        if (result != null && result.size() > 0) {
            return Optional.of(cache(repairDropData(result)));
        }
        return Optional.empty();
    }

    private List<WfsDrop> repairDropData(List<WfsDrop> data) {
        Pattern primePartPattern = Pattern.compile("(\\w+) P\\. (\\w+ )?BP");
        Pattern blueprintPattern = Pattern.compile("(.*) BP");
        for (WfsDrop drop : data) {
            Matcher primePartMatcher = primePartPattern.matcher(drop.getItem());
            if (primePartMatcher.matches()) {
                drop.setItem(primePartMatcher.group(1) + " Prime "
                        + StringUtils.defaultString(primePartMatcher.group(2), "") + "Blueprint");
            } else {
                Matcher blueprintMatcher = blueprintPattern.matcher(drop.getItem());
                if (blueprintMatcher.matches()) {
                    drop.setItem(blueprintMatcher.group(1) + " Blueprint");
                }
            }
        }
        return data;
    }

    private @NonNull List<WfsDrop> cache(@NonNull List<WfsDrop> names) {
        log.debug("Cache data: {}", names);
        readCache.put(KEY, names);
        backup.put(KEY, names);
        return names;
    }

    private List<WfsDrop> getFromBackup() {
        log.trace("getFromBackup()");
        return backup.get(KEY);
    }

}
