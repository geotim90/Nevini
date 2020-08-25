package de.nevini.modules.warframe.fissures;

import de.nevini.api.wfs.model.worldstate.WfsFissure;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FissuresCommand extends Command {

    public FissuresCommand() {
        super(CommandDescriptor.builder()
                .keyword("fissures")
                .aliases(new String[]{"fissure"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active fissures using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve fissure data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        // collect data
        List<WfsFissure> fissures = worldState.getFissures().stream()
                .filter(fissure -> fissure.getExpiry().isAfter(OffsetDateTime.now()))
                .sorted(Comparator.comparing(WfsFissure::getTierNum).thenComparing(WfsFissure::getExpiry))
                .collect(Collectors.toList());
        List<String> tiers = fissures.stream().map(WfsFissure::getTier).distinct().collect(Collectors.toList());

        // build embed
        EmbedBuilder embed = event.createEmbedBuilder()
                .setTitle("Void Fissures")
                .setFooter("warframestat.us", "https://warframestat.us/wfcd_logo_color.png");
        for (String tier : tiers) {
            StringBuilder builder = new StringBuilder();
            fissures.stream().filter(fissure -> tier.equals(fissure.getTier())).forEach(fissure -> builder
                    .append("[").append(WfsFormatter.formatEta(fissure.getExpiry())).append("] ")
                    .append(fissure.getEnemy()).append(" **").append(fissure.getMissionType()).append("** ")
                    .append(fissure.getNode()).append("\n"));
            embed.addField(tier, builder.toString(), false);
        }
        event.reply(embed, event::complete);
    }

}
