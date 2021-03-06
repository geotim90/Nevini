package de.nevini.modules.warframe.commands.conclave;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsConclaveChallenge;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.util.WfsFormatter;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.EmbedBuilder;
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

        List<String> modes = challenges.stream().map(WfsConclaveChallenge::getMode).distinct()
                .collect(Collectors.toList());

        EmbedBuilder embed = event.createEmbedBuilder()
                .setTitle("Conclave Challenges")
                .setFooter("warframestat.us", "https://warframestat.us/wfcd_logo_color.png");
        for (String mode : modes) {
            StringBuilder builder = new StringBuilder();
            challenges.stream().filter(e -> mode.equals(e.getMode())).forEach(challenge -> builder.append("[")
                    .append(WfsFormatter.formatEta(challenge.getExpiry())).append("] ")
                    .append(challenge.getAsString()).append("\n"));
            embed.addField(mode, builder.toString(), false);
        }
        event.reply(embed, event::complete);
    }

}
