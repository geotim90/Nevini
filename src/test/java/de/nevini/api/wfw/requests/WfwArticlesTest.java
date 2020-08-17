package de.nevini.api.wfw.requests;

import de.nevini.api.ApiResponse;
import de.nevini.api.wfw.model.UnexpandedArticle;
import de.nevini.api.wfw.model.UnexpandedListArticleResultSet;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class WfwArticlesTest extends WfwApiProvider {

    @Test
    public void testParser() throws IOException {
        WfwArticleRequest request = WfwArticleRequest.builder().build();
        UnexpandedListArticleResultSet result;
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("List.json")
        )) {
            result = request.parseStream(reader);
        }

        // make sure all data is present
        Assert.assertEquals(7125, result.getItems().size());

        // make sure all data was parsed correctly
        UnexpandedArticle item = result.getItems().get(0);
        Assert.assertEquals("002-ER", item.getTitle());
        Assert.assertEquals("/wiki/002-ER", item.getUrl());
    }

    @Test
    public void testBlankRequest() {
        ApiResponse<UnexpandedListArticleResultSet> response = getWfwApi().getArticles(WfwArticleRequest.builder()
                .build());
        Assert.assertTrue(response.toString(), response.isOk());
    }

}
