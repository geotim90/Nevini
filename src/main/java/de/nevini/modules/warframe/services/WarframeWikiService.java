package de.nevini.modules.warframe.services;

import de.nevini.core.locators.Locatable;
import de.nevini.modules.warframe.api.wfw.model.UnexpandedArticle;
import de.nevini.modules.warframe.cache.wfw.WfwArticleDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WarframeWikiService implements Locatable {

    private final WfwArticleDataService articleDataService;

    public WarframeWikiService(@Autowired WfwArticleDataService articleDataService) {
        this.articleDataService = articleDataService;
    }

    public synchronized List<UnexpandedArticle> getArticles() {
        return articleDataService.get();
    }

}
