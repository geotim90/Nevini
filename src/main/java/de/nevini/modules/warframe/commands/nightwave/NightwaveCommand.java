package de.nevini.modules.warframe.commands.nightwave;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsActiveChallenge;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.util.WfsFormatter;
import de.nevini.modules.warframe.services.WarframeStatusService;
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
                .node(Node.WARFRAME)
                .description("displays the currently active Nightwave challenges using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve Nightwave data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
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
