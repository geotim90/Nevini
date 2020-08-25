package de.nevini.data.wfw;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nevini.api.ApiResponse;
import de.nevini.api.wfw.WarframeWikiApi;
import de.nevini.api.wfw.model.UnexpandedArticle;
import de.nevini.api.wfw.model.UnexpandedListArticleResultSet;
import de.nevini.api.wfw.requests.WfwArticleRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WfwArticleDataService {

    private static final String KEY = "pc|en";

    private final Cache<String, List<UnexpandedArticle>> readCache;
    private final WarframeWikiApi api;
    private final Map<String, List<UnexpandedArticle>> backup;

    public WfwArticleDataService(@Autowired WarframeWikiApiProvider apiProvider) {
        this.readCache = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(15))
                .build();
        this.api = apiProvider.getApi();
        this.backup = new ConcurrentHashMap<>();
    }

    public List<UnexpandedArticle> get() {
        log.trace("get()");
        return getFromReadCache().orElseGet(() ->
                getFromApi().orElse(getFromBackup())
        );
    }

    private @NonNull Optional<List<UnexpandedArticle>> getFromReadCache() {
        log.trace("getFromReadCache()");
        return Optional.ofNullable(readCache.getIfPresent(KEY));
    }

    private @NonNull Optional<List<UnexpandedArticle>> getFromApi() {
        log.trace("getFromApi()");
        ApiResponse<UnexpandedListArticleResultSet> response = api.getArticles(WfwArticleRequest.builder().build());
        UnexpandedListArticleResultSet result = response.getResult();
        if (result != null && result.getItems().size() > 0) {
            return Optional.of(cache(result.getItems().stream().map(
                    e -> UnexpandedArticle.builder()
                            .title(e.getTitle())
                            .url(result.getBasepath() + e.getUrl())
                            .build()
            ).collect(Collectors.toList())));
        }
        return Optional.empty();
    }

    private @NonNull List<UnexpandedArticle> cache(@NonNull List<UnexpandedArticle> names) {
        log.debug("Cache data: {}", names);
        readCache.put(KEY, names);
        backup.put(KEY, names);
        return names;
    }

    private List<UnexpandedArticle> getFromBackup() {
        log.trace("getFromBackup()");
        return backup.get(KEY);
    }

}
