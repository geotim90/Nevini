package de.nevini.modules.warframe.commands.bounties;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsJob;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsSyndicateMissions;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.util.WfsFormatter;
import de.nevini.modules.warframe.resolvers.WarframeResolvers;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BountiesCommand extends Command {

    public BountiesCommand() {
        super(CommandDescriptor.builder()
                .keyword("bounties")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays active bounties using data from warframestat.us")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.BOUNTIES_FACTION.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.BOUNTIES_FACTION.resolveArgumentOrOptionOrInput(event,
                faction -> acceptFaction(event, faction));
    }

    private void acceptFaction(CommandEvent event, String faction) {
        // retrieve syndicate data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        Optional<WfsSyndicateMissions> result = worldState.getSyndicateMissions().stream()
                .filter(e -> faction.startsWith(e.getSyndicate())).findAny();
        if (result.isPresent()) {
            WfsSyndicateMissions missions = result.get();
            StringBuilder builder = new StringBuilder("**" + missions.getSyndicate() + "** - "
                    + WfsFormatter.formatEta(missions.getExpiry()));
            for (WfsJob job : missions.getJobs()) {
                builder.append("\n\n**").append(job.getType()).append("** - Level ").append(job.getEnemyLevels().get(0))
                        .append('-').append(job.getEnemyLevels().get(1)).append('\n')
                        .append(StringUtils.join(job.getStandingStages(), " + ")).append(" standing\n")
                        .append(StringUtils.join(job.getRewardPool(), " / "));
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}
