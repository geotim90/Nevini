package de.nevini.modules.warframe.commands.anomalies;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsSentientOutposts;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsSentientOutpostsMission;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.util.WfsFormatter;
import de.nevini.modules.warframe.services.WarframeStatusService;
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
        WarframeStatusService service = event.locate(WarframeStatusService.class);
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
