package de.nevini.modules.warframe.commands.alerts;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsAlert;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsAlertMission;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.util.WfsFormatter;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlertsCommand extends Command {

    public AlertsCommand() {
        super(CommandDescriptor.builder()
                .keyword("alerts")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active alerts using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve alert data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        List<WfsAlert> alerts = worldState.getAlerts().stream()
                .filter(e -> e.getActivation().isBefore(now) && e.getExpiry().isAfter(now))
                .collect(Collectors.toList());

        if (alerts.isEmpty()) {
            event.reply("No currently active alerts", event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (WfsAlert alert : alerts) {
            WfsAlertMission mission = alert.getMission();
            builder.append("\n\n**").append(mission.getNode()).append("** - ").append(mission.getDescription())
                    .append("\n").append(mission.getType()).append(" (").append(mission.getMinEnemyLevel())
                    .append(" - ").append(mission.getMaxEnemyLevel()).append(") - ").append(mission.getFaction())
                    .append("\n").append(mission.getReward().getAsString()).append("\n")
                    .append(WfsFormatter.formatEta(alert.getExpiry()));
        }
        event.reply(builder.toString(), event::complete);
    }

}
