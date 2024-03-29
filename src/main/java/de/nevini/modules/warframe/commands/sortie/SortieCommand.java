package de.nevini.modules.warframe.commands.sortie;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.api.wfs.util.WfsFormatter;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class SortieCommand extends Command {

    public SortieCommand() {
        super(CommandDescriptor.builder()
                .keyword("sortie")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active sorties using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve sortie data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        event.reply("**" + worldState.getSortie().getBoss() + "** - "
                        + WfsFormatter.formatEta(worldState.getSortie().getExpiry())
                        + "\n\n**" + worldState.getSortie().getVariants().get(0).getNode()
                        + "** Level 50-60\nMission: **" + worldState.getSortie().getVariants().get(0).getMissionType()
                        + "**\nConditions: **" + worldState.getSortie().getVariants().get(0).getModifier()
                        + "**\n\n**" + worldState.getSortie().getVariants().get(1).getNode()
                        + "** Level 65-80\nMission: **" + worldState.getSortie().getVariants().get(1).getMissionType()
                        + "**\nConditions: **" + worldState.getSortie().getVariants().get(1).getModifier()
                        + "**\n\n**" + worldState.getSortie().getVariants().get(2).getNode()
                        + "** Level 80-100\nMission: **" + worldState.getSortie().getVariants().get(2).getMissionType()
                        + "**\nConditions: **" + worldState.getSortie().getVariants().get(2).getModifier() + "**",
                event::complete);
    }

}
