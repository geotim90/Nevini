package de.nevini.modules.warframe.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.warframe.api.wfw.model.UnexpandedArticle;
import de.nevini.modules.warframe.services.WarframeWikiService;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfwArticleResolver extends OptionResolver<UnexpandedArticle> {

    WfwArticleResolver() {
        super("page name", new Pattern[]{
                Pattern.compile("(?i)(?:--|//)(?:page|article)(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--page] <name>" : "--page <name>")
                .description("Refers to " + (list ? "all articles" : "an article") + " with a matching name."
                        + (argument ? "\nThe `--page` flag is optional if this option is provided first." : ""))
                .keyword("--page")
                .aliases(new String[]{"//page", "--article", "//article"})
                .build();
    }

    @Override
    public List<UnexpandedArticle> findSorted(@NonNull CommandEvent event, String query) {
        WarframeWikiService service = event.locate(WarframeWikiService.class);
        return Finder.findLenient(service.getArticles(), UnexpandedArticle::getTitle, query).stream()
                .sorted(Comparator.comparing(UnexpandedArticle::getTitle)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(UnexpandedArticle item) {
        return item.getTitle();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(UnexpandedArticle item) {
        return item.getUrl();
    }


}
