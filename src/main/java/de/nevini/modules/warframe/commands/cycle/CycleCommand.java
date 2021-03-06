package de.nevini.modules.warframe.commands.cycle;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsCambionCycle;
import de.nevini.modules.warframe.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandReaction;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CycleCommand extends Command {

    public CycleCommand() {
        super(CommandDescriptor.builder()
                .keyword("cycle")
                .aliases(new String[]{"cycles", "world-cycle", "world-cycles"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays the current world cycles using data from warframestat.us")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // retrieve world cycles data from service
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        WfsWorldState worldState = service.getWorldState();

        // abort if no data is available
        if (worldState == null) {
            event.reply(CommandReaction.ERROR, event::complete);
            return;
        }

        event.reply("**World Cycles**\nPlains of Eidolon: " + reformat(worldState.getCetusCycle().getShortString())
                + "\nOrb Vallis: " + reformat(worldState.getVallisCycle().getShortString())
                + "\nCambion Drift: " + formatCambion(worldState.getCambionCycle()), event::complete);
    }

    private static String reformat(String shortString) {
        Matcher matcher = Pattern.compile("(.+) to (.+)").matcher(shortString);
        if (matcher.matches()) {
            return matcher.group(2) + " in " + matcher.group(1);
        } else {
            return shortString;
        }
    }

    private static String formatCambion(WfsCambionCycle data) {
        String nextActive = "vome".equals(data.getActive()) ? "Fass" : "Vome";
        return nextActive + " in " + Formatter.formatShortUnitsBetween(OffsetDateTime.now(), data.getExpiry());
    }

}
