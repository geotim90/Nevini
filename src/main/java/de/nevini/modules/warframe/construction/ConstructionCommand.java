package de.nevini.modules.warframe.construction;

import de.nevini.api.wfs.model.worldstate.WfsConstructionProgress;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class ConstructionCommand extends Command {

    public ConstructionCommand() {
        super(CommandDescriptor.builder()
                .keyword("construction")
                .aliases(new String[]{"fomorian", "razorback"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the current construction progress using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve construction data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        WfsConstructionProgress progress = worldState.getConstructionProgress();
        event.reply("**Fomorian**: " + progress.getFomorianProgress() + "%\n**Razorback**: "
                + progress.getRazorbackProgress() + "%", event::complete);
    }

}
