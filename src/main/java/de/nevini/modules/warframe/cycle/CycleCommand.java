package de.nevini.modules.warframe.cycle;

import de.nevini.api.wfs.model.worldstate.WfsWorldState;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatusService;
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
