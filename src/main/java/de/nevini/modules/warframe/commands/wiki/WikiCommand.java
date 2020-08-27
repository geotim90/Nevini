package de.nevini.modules.warframe.commands.wiki;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfw.model.UnexpandedArticle;
import de.nevini.modules.warframe.resolvers.WarframeResolvers;
import de.nevini.util.command.CommandOptionDescriptor;
import org.springframework.stereotype.Component;

@Component
public class WikiCommand extends Command {

    public WikiCommand() {
        super(CommandDescriptor.builder()
                .keyword("wiki")
                .aliases(new String[]{"wikia"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("searches for articles on warframe.fandom.com by page name")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.ARTICLE.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.ARTICLE.resolveArgumentOrOptionOrInput(event, item -> acceptArticle(event, item));
    }

    private void acceptArticle(CommandEvent event, UnexpandedArticle article) {
        event.reply(article.getUrl(), event::complete);
    }

}
