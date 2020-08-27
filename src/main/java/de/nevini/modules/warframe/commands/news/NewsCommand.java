package de.nevini.modules.warframe.commands.news;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsNews;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class NewsCommand extends Command {

    public NewsCommand() {
        super(CommandDescriptor.builder()
                .keyword("news")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active news using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve news data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        worldState.getNews().stream().sorted(Comparator.comparing(WfsNews::getDate).reversed())
                .forEach(news -> builder.append(news.getAsString()).append('\n'));

        event.reply(event.createEmbedBuilder()
                .setTitle("Warframe News", "https://www.warframe.com/")
                .setFooter("warframestat.us", "https://warframestat.us/wfcd_logo_color.png")
                .setTimestamp(worldState.getTimestamp())
                .setDescription(builder.toString()), event::complete);
    }

}
