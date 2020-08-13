package de.nevini.modules.warframe.baro;

import de.nevini.api.wfs.model.worldstate.WfsInventory;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class BaroCommand extends Command {

    public BaroCommand() {
        super(CommandDescriptor.builder()
                .keyword("baro")
                .aliases(new String[]{"void-trader"})
                .guildOnly(false)
                .node(Node.WARFRAME)
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
            // Void Trader active
            StringBuilder builder = new StringBuilder("**Void Trader**: " + worldState.getVoidTrader().getLocation()
                    + " - " + WfsFormatter.formatEta(worldState.getVoidTrader().getExpiry()) + "\n");
            for (WfsInventory inventory : worldState.getVoidTrader().getInventory()) {
                builder.append("\n**").append(inventory.getItem()).append("** (")
                        .append(inventory.getDucats()).append(" ducats, ")
                        .append(Formatter.formatLargeInteger(inventory.getCredits())).append(" credits)");
            }
            event.reply(builder.toString(), event::complete);
        } else {
            // Void Trader inactive
            event.reply("**Void Trader**: " + worldState.getVoidTrader().getLocation() + " - "
                    + WfsFormatter.formatEta(worldState.getVoidTrader().getActivation()), event::complete);
        }
    }

}
