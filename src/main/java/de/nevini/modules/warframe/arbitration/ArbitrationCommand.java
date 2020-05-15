package de.nevini.modules.warframe.arbitration;

import de.nevini.api.wfs.model.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ArbitrationCommand extends Command {

    public ArbitrationCommand() {
        super(CommandDescriptor.builder()
                .keyword("arbitration")
                .guildOnly(false)
                .node(Node.WARFRAME_SORTIE)
                .description("displays the currently active arbitration using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve arbitration data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        event.reply("**" + worldState.getArbitration().getNode() + "** Level 60-80\n**"
                + worldState.getArbitration().getType() + "** - **" + worldState.getArbitration().getEnemy()
                + "** (Arbitration)\n" + formatTimeRemaining(worldState.getArbitration().getExpiry()), event::complete);
    }

    private String formatTimeRemaining(OffsetDateTime expiry) {
        long seconds = OffsetDateTime.now().until(expiry, ChronoUnit.SECONDS);

        if (seconds < 0) {
            return "";
        }

        if (seconds < 60) {
            return seconds + "s";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + "m " + (seconds % 60) + "s";
        }

        long hours = minutes / 60;
        return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
    }

}
