package de.nevini.modules.warframe.anomalies;

import de.nevini.api.wfs.model.WfsSentientOutposts;
import de.nevini.api.wfs.model.WfsSentientOutpostsMission;
import de.nevini.api.wfs.model.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class AnomaliesCommand extends Command {

    public AnomaliesCommand() {
        super(CommandDescriptor.builder()
                .keyword("anomalies")
                .aliases(new String[]{"anomaly", "sentient-anomaly", "sentient", "sentient-outpost", "outpost", "murex"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active anomalies using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve anomaly data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        WfsSentientOutposts anomaly = worldState.getSentientOutposts();
        if (anomaly.getExpiry().isAfter(now)) {
            WfsSentientOutpostsMission mission = anomaly.getMission();
            if (anomaly.getActivation().isBefore(now)) {
                // active
                event.reply("**" + mission.getNode() + "**\n"
                        + mission.getFaction() + " - " + mission.getType() + "\n"
                        + WfsFormatter.formatEta(anomaly.getExpiry()), event::complete);
            } else {
                // not yet active
                event.reply("**" + mission.getNode() + "**\n"
                        + mission.getFaction() + " - " + mission.getType() + "\n"
                        + WfsFormatter.formatEta(anomaly.getActivation()), event::complete);
            }
        } else {
            // inactive
            event.reply("No currently active anomalies", event::complete);
        }

    }

}
