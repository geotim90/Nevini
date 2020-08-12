package de.nevini.modules.warframe.syndicate;

import de.nevini.api.wfs.model.worldstate.WfsSyndicateMissions;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.warframe.WarframeResolvers;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SyndicateCommand extends Command {

    public SyndicateCommand() {
        super(CommandDescriptor.builder()
                .keyword("syndicate")
                .guildOnly(false)
                .node(Node.WARFRAME_STAT_US)
                .description("displays syndicate nodes for a syndicate using data from warframestat.us")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.SYNDICATE_FACTION.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.SYNDICATE_FACTION.resolveArgumentOrOptionOrInput(event,
                faction -> acceptFaction(event, faction));
    }

    private void acceptFaction(CommandEvent event, String faction) {
        // retrieve syndicate data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        Optional<WfsSyndicateMissions> result = worldState.getSyndicateMissions().stream()
                .filter(e -> e.getSyndicate().equals(faction)).findAny();
        if (result.isPresent()) {
            WfsSyndicateMissions missions = result.get();
            StringBuilder builder = new StringBuilder("**" + missions.getSyndicate() + "** - "
                    + WfsFormatter.formatEta(missions.getExpiry()) + "\n");
            for (String node : missions.getNodes()) {
                builder.append('\n').append(node);
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}
