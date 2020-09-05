package de.nevini.modules.warframe.commands.invasions;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsInvasion;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.services.WarframeStatusService;
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
        WarframeStatusService service = event.locate(WarframeStatusService.class);
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
