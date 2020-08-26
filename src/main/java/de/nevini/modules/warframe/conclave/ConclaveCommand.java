package de.nevini.modules.warframe.conclave;

import de.nevini.api.wfs.model.worldstate.WfsConclaveChallenge;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConclaveCommand extends Command {

    public ConclaveCommand() {
        super(CommandDescriptor.builder()
                .keyword("conclave")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active Conclave challenges using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve Conclave data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        OffsetDateTime now = OffsetDateTime.now();
        List<WfsConclaveChallenge> challenges = worldState.getConclaveChallenges().stream()
                .filter(e -> e.getActivation().isBefore(now) && e.getExpiry().isAfter(now)
                        && Boolean.FALSE.equals(e.getRootChallenge()))
                .collect(Collectors.toList());

        if (challenges.isEmpty()) {
            event.reply("No currently active challenges", event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (WfsConclaveChallenge challenge : challenges) {
            builder.append("\n\n**").append(challenge.getMode()).append("**\n")
                    .append(challenge.getAsString()).append("\n")
                    .append(WfsFormatter.formatEta(challenge.getExpiry()));
        }
        event.reply(builder.toString(), event::complete);
    }

}
