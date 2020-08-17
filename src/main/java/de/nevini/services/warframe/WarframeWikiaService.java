package de.nevini.services.warframe;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfw.WarframeWikiaApi;
import de.nevini.api.wfw.model.UnexpandedArticle;
import de.nevini.api.wfw.model.UnexpandedListArticleResultSet;
import de.nevini.api.wfw.requests.WfwArticleRequest;
import de.nevini.locators.Locatable;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WarframeWikiaService implements Locatable {

    private final WarframeWikiaApi api = new WarframeWikiaApi(new OkHttpClient.Builder().build());

    private List<UnexpandedArticle> articles = null;

    public synchronized List<UnexpandedArticle> getArticles() {
        ApiResponse<UnexpandedListArticleResultSet> result = api.getArticles(WfwArticleRequest.builder().build());
        ApiResponse<List<UnexpandedArticle>> mappedResult = result.map(r -> r.getItems().stream().map(
                e -> UnexpandedArticle.builder()
                        .title(e.getTitle())
                        .url(r.getBasepath() + e.getUrl())
                        .build()
        ).collect(Collectors.toList()));
        return articles = mappedResult.orElse(ApiResponse.ok(articles)).getResult();
    }

}
