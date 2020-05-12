package de.nevini.modules.warframe.baro;

import de.nevini.api.wfs.model.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class BaroCommand extends Command {

    public BaroCommand() {
        super(CommandDescriptor.builder()
                .keyword("baro")
                .guildOnly(false)
                .node(Node.WARFRAME_BARO)
                .description("displays the current status of the Void Trader using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve Void Trader data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        if (Boolean.TRUE.equals(worldState.getVoidTrader().getActive())) {
            // TODO Void Trader active

        } else {
            // Void Trader inactive
            event.reply("**Void Trader**: " + worldState.getVoidTrader().getLocation() + " - "
                    + worldState.getVoidTrader().getStartString(), event::complete);
        }
    }

}
