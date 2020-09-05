package de.nevini.modules.warframe.commands.simaris;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class SimarisCommand extends Command {

    public SimarisCommand() {
        super(CommandDescriptor.builder()
                .keyword("simaris")
                .aliases(new String[]{"sanctuary"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays current Sanctuary status using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve Simaris data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        event.reply(worldState.getSimaris().getAsString(), event::complete);
    }

}
