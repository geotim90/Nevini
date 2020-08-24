package de.nevini.modules.warframe.drops;

import de.nevini.api.wfs.model.drops.WfsDrop;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DropsCommand extends Command {

    public DropsCommand() {
        super(CommandDescriptor.builder()
                .keyword("drops")
                .aliases(new String[]{"drop"})
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays item locations using data from warframestat.us")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--item] <name>")
                                .description("Refers to all items with a matching name."
                                        + "\nThe `--item` flag is optional if this option is provided first.")
                                .keyword("--item")
                                .aliases(new String[]{"//item"})
                                .build()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        String search = event.getArgument();
        if (StringUtils.isEmpty(search)) {
            return;
        }

        WarframeStatsService service = event.locate(WarframeStatsService.class);
        List<WfsDrop> drops = service.getDrops(search).stream().sorted(Comparator.comparing(WfsDrop::getItem)
                .thenComparing(e -> -e.getChance()).thenComparing(WfsDrop::getPlace)).collect(Collectors.toList());
        List<String> items = drops.stream().map(WfsDrop::getItem).distinct().collect(Collectors.toList());

        if (drops.isEmpty()) {
            event.reply(CommandReaction.WARNING, "I could not find any drops that matched your input!",
                    event::complete);
            return;
        }

        EmbedBuilder embed = event.createEmbedBuilder()
                .setTitle("Item Locations")
                .setFooter("warframestat.us", "https://warframestat.us/wfcd_logo_color.png");
        for (String item : items) {
            StringBuilder builder = new StringBuilder();
            drops.stream().filter(drop -> item.equals(drop.getItem())).forEach(drop -> builder
                    .append(drop.getPlace()).append(" - ").append(drop.getRarity()).append(" (")
                    .append(Formatter.formatPercent(drop.getChance() / 100)).append(")\n"));
            embed.addField(item, builder.toString(), false);
        }
        event.reply(embed, event::complete);
    }

}
