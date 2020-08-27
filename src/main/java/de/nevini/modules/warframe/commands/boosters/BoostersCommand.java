package de.nevini.modules.warframe.commands.boosters;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsGlobalUpgrade;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class BoostersCommand extends Command {

    public BoostersCommand() {
        super(CommandDescriptor.builder()
                .keyword("boosters")
                .aliases(new String[]{"upgrades", "global-upgrades"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active boosters using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve global upgrade data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (WfsGlobalUpgrade booster : worldState.getGlobalUpgrades()) {
            builder.append(booster.getDesc()).append('\n');
        }
        if (builder.length() == 0) {
            event.reply("No currently active boosters", event::complete);
        } else {
            event.reply(builder.toString(), event::complete);
        }
    }

}
