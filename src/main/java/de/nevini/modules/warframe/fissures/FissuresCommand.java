package de.nevini.modules.warframe.fissures;

import de.nevini.api.wfs.model.WfsFissure;
import de.nevini.api.wfs.model.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class FissuresCommand extends Command {

    public FissuresCommand() {
        super(CommandDescriptor.builder()
                .keyword("fissures")
                .guildOnly(false)
                .node(Node.WARFRAME_STAT_US)
                .description("displays the currently active fissures using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve fissure data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        worldState.getFissures().stream()
                .sorted(Comparator.comparing(WfsFissure::getTierNum).thenComparing(WfsFissure::getExpiry))
                .forEach(fissure -> builder.append("\n\n**").append(fissure.getNode()).append("**\n**")
                        .append(fissure.getMissionType()).append("** - **").append(fissure.getEnemy())
                        .append("**\n").append(fissure.getTier()).append(" Fissure\n")
                        .append(WfsFormatter.formatEta(fissure.getExpiry())));
        event.reply(builder.toString(), event::complete);
    }

}
