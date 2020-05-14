package de.nevini.modules.warframe.riven;

import de.nevini.api.wfs.model.WfsRiven;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.warframe.WarframeResolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RivenCommand extends Command {

    public RivenCommand() {
        super(CommandDescriptor.builder()
                .keyword("riven")
                .aliases(new String[]{"rivens"})
                .guildOnly(false)
                .node(Node.WARFRAME_RIVEN)
                .description("displays trading statistics for rivens using data from warframestat.us")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.RIVEN.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.RIVEN.resolveArgumentOrOptionOrInput(event, item -> acceptRiven(event, item));
    }

    private void acceptRiven(CommandEvent event, WfsRiven item) {
        event.reply("Statistics from warframestat.us for **" + item.getDisplayName() + "**"
                + "\n```c"
                + "\nAverage:     " + formatPrice(item.getAvg())
                + "\nMedian:      " + formatPrice(item.getMedian())
                + "\nMaximum:     " + formatPrice(item.getMax())
                + "\nMinimum:     " + formatPrice(item.getMin())
                + "\nPopularity:  " + formatPopularity(item.getPop())
                + "\n```", event::complete);
    }

    private String formatPrice(Integer value) {
        if (value == null || value == 0) {
            return "     -    ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 6) + "   p";
        }
    }

    private String formatPrice(Float value) {
        if (value == null || value == 0) {
            return "     -    ";
        } else {
            String text = Formatter.formatDecimal(value);
            // right pad
            if (!text.contains(".")) {
                text += "   ";
            } else if (text.lastIndexOf('.') == text.length() - 2) {
                text += " ";
            }
            // left pad
            return StringUtils.leftPad(text, 9) + "p";
        }
    }

    private String formatPopularity(Integer value) {
        if (value == null || value == 0) {
            return "     -    ";
        } else {
            return StringUtils.leftPad(Formatter.formatInteger(value), 6) + "   %";
        }
    }

}
