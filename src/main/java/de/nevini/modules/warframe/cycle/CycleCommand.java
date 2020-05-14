package de.nevini.modules.warframe.cycle;

import de.nevini.api.wfs.model.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CycleCommand extends Command {

    public CycleCommand() {
        super(CommandDescriptor.builder()
                .keyword("cycle")
                .aliases(new String[]{"cycles", "world-cycle", "world-cycles"})
                .guildOnly(false)
                .node(Node.WARFRAME_CYCLE)
                .description("displays the current World Cycles using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve Void Trader data from service
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        // Void Trader inactive
        event.reply("**World Cycles**\nPlains of Eidolon: " + reformat(worldState.getCetusCycle().getShortString())
                + "\nOrb Vallis: " + reformat(worldState.getVallisCycle().getShortString()), event::complete);
    }

    private static String reformat(String shortString) {
        Matcher matcher = Pattern.compile("(.+) to (.+)").matcher(shortString);
        if (matcher.matches()) {
            return matcher.group(2) + " in " + matcher.group(1);
        } else {
            return shortString;
        }
    }

}
