package de.nevini.modules.warframe.invasions;

import de.nevini.api.wfs.model.worldstate.WfsInvasion;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class InvasionsCommand extends Command {

    public InvasionsCommand() {
        super(CommandDescriptor.builder()
                .keyword("invasions")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active invasions using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve invasion data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (WfsInvasion invasion : worldState.getInvasions()) {
            builder.append("\n\n**").append(invasion.getNode()).append("**: **").append(invasion.getDesc())
                    .append("**\nAttacker: ").append(invasion.getAttackingFaction());
            if (!StringUtils.isEmpty(invasion.getAttackerReward().getItemString())) {
                builder.append(" - **").append(invasion.getAttackerReward().getItemString()).append("**");
            }
            builder.append("\nDefender: ").append(invasion.getDefendingFaction());
            if (!StringUtils.isEmpty(invasion.getDefenderReward().getItemString())) {
                builder.append(" - **").append(invasion.getDefenderReward().getItemString()).append("**");
            }
        }
        event.reply(builder.toString(), event::complete);
    }

}
