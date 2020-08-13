package de.nevini.modules.warframe.events;

import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class EventsCommand extends Command {

    public EventsCommand() {
        super(CommandDescriptor.builder()
                .keyword("events")
                .aliases(new String[]{"event"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays ongoing events using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve event data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        worldState.getEvents().forEach(e -> builder.append(cleanUp(e.getAsString())).append("\n\n"));

        event.reply(builder.toString(), event::complete);
    }

    private String cleanUp(String event) {
        return event.replace(" : undefined", "");
    }

}
