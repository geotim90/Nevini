package de.nevini.modules.warframe.arbitration;

import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class ArbitrationCommand extends Command {

    public ArbitrationCommand() {
        super(CommandDescriptor.builder()
                .keyword("arbitration")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active arbitration using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve arbitration data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        event.reply("**" + worldState.getArbitration().getNode() + "** Level 60-80\n**"
                + worldState.getArbitration().getType() + "** - **" + worldState.getArbitration().getEnemy()
                + "** (Arbitration)\n" + WfsFormatter.formatEta(worldState.getArbitration().getExpiry()), event::complete);
    }

}
