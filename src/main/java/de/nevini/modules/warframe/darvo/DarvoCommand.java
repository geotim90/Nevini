package de.nevini.modules.warframe.darvo;

import de.nevini.api.wfs.model.worldstate.WfsDailyDeal;
import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.api.wfs.util.WfsFormatter;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatusService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

@Component
public class DarvoCommand extends Command {

    public DarvoCommand() {
        super(CommandDescriptor.builder()
                .keyword("darvo")
                .aliases(new String[]{"darvo-deal", "daily-deal"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the currently active Darvo Deal using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve arbitration data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        for (WfsDailyDeal dailyDeal : worldState.getDailyDeals()) {
            event.reply("**" + dailyDeal.getItem() + "** ~~" + dailyDeal.getOriginalPrice() + "p~~ "
                    + dailyDeal.getSalePrice() + "p\n" + (dailyDeal.getSold() < dailyDeal.getTotal()
                    ? (dailyDeal.getTotal() - dailyDeal.getSold()) + " left"
                    : "**OUT OF STOCK**") + " - " + WfsFormatter.formatEta(dailyDeal.getExpiry()), event::complete);
            break; // stop after first
        }
    }

}
