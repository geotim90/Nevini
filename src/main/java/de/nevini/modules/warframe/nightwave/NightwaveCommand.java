package de.nevini.modules.warframe.nightwave;

import de.nevini.api.wfs.model.WfsActiveChallenge;
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
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NightwaveCommand extends Command {

    public NightwaveCommand() {
        super(CommandDescriptor.builder()
                .keyword("nightwave")
                .guildOnly(false)
                .node(Node.WARFRAME_STAT_US)
                .description("displays the currently active Nightwave challenges using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve Nightwave data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null || worldState.getNightwave() == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        List<WfsActiveChallenge> challenges = worldState.getNightwave().getActiveChallenges().stream()
                .filter(e -> e.getActivation().isBefore(now) && e.getExpiry().isAfter(now))
                .collect(Collectors.toList());

        if (challenges.isEmpty()) {
            event.reply("No currently active challenges", event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (WfsActiveChallenge challenge : challenges) {
            builder.append("\n\n**").append(challenge.getTitle()).append("** - ")
                    .append(WfsFormatter.formatEta(challenge.getExpiry())).append("\n")
                    .append(challenge.getReputation()).append(" - ");
            if (Boolean.TRUE.equals(challenge.getIsElite())) {
                builder.append("Elite ");
            }
            builder.append(Boolean.TRUE.equals(challenge.getIsDaily()) ? "Daily" : "Weekly").append("\n")
                    .append(challenge.getDesc());
        }
        event.reply(builder.toString(), event::complete);
    }

}
