package de.nevini.modules.warframe.commands.riven;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRiven;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRivenData;
import de.nevini.modules.warframe.resolvers.WarframeResolvers;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RivenCommand extends Command {

    public RivenCommand() {
        super(CommandDescriptor.builder()
                .keyword("riven")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays trading statistics for rivens using data from warframestat.us")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.RIVEN.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            WarframeResolvers.RIVEN.resolveArgumentOrOptionOrInput(event, item -> acceptRiven(event, item));
        } catch (NullPointerException e) {
            event.reply(CommandReaction.ERROR, "Unable to retrieve riven data from warframestat.us", event::complete);
        }
    }

    private void acceptRiven(CommandEvent event, WfsRiven item) {
        WfsRivenData unrolled = item.getUnrolled();
        WfsRivenData rerolled = item.getRerolled();
        if (item.getRerolled() == null) {
            event.reply("Statistics from warframestat.us for **" + item.getDisplayName() + "**"
                    + "\n```c"
                    + "\nAverage:     " + formatPrice(unrolled.getAvg())
                    + "\nMedian:      " + formatPrice(unrolled.getMedian())
                    + "\nMaximum:     " + formatPrice(unrolled.getMax())
                    + "\nMinimum:     " + formatPrice(unrolled.getMin())
                    + "\nPopularity:  " + formatPopularity(unrolled.getPop())
                    + "\n```", event::complete);
        } else {
            event.reply("Statistics from warframestat.us for **" + item.getDisplayName() + "**"
                    + "\n```c"
                    + "\n             Unrolled      Rerolled"
                    + "\nAverage:     " + formatPrice(unrolled.getAvg()) + "   " + formatPrice(rerolled.getAvg())
                    + "\nMedian:      " + formatPrice(unrolled.getMedian()) + "   " + formatPrice(rerolled.getMedian())
                    + "\nMaximum:     " + formatPrice(unrolled.getMax()) + "   " + formatPrice(rerolled.getMax())
                    + "\nMinimum:     " + formatPrice(unrolled.getMin()) + "   " + formatPrice(rerolled.getMin())
                    + "\nPopularity:  " + formatPopularity(unrolled.getPop()) + "   " + formatPopularity(rerolled.getPop())
                    + "\n```", event::complete);
        }
    }

    private String formatPrice(Integer value) {
        if (value == null || value == 0) {
            return "      -    ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 7) + "   p";
        }
    }

    private String formatPrice(Float value) {
        if (value == null || value == 0) {
            return "      -    ";
        } else {
            String text = Formatter.formatDecimal(value);
            // right pad
            if (!text.contains(".")) {
                text += "   ";
            } else if (text.lastIndexOf('.') == text.length() - 2) {
                text += " ";
            }
            // left pad
            return StringUtils.leftPad(text, 10) + "p";
        }
    }

    private String formatPopularity(Integer value) {
        if (value == null || value == 0) {
            return "      -    ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 7) + "   %";
        }
    }

}
