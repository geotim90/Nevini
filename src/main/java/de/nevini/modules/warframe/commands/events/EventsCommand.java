package de.nevini.modules.warframe.commands.events;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.services.WarframeStatusService;
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
        WarframeStatusService service = event.locate(WarframeStatusService.class);
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
