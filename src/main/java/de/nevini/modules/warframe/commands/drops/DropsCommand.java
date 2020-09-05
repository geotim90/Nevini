package de.nevini.modules.warframe.commands.drops;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.warframe.api.wfs.model.drops.WfsDrop;
import de.nevini.modules.warframe.resolvers.WarframeResolvers;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
                        WarframeResolvers.DROP.describe(true, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.DROP.resolveListArgumentOrOptionOrInput(event, drops -> acceptDrops(event, drops));
    }

    private void acceptDrops(CommandEvent event, List<WfsDrop> drops) {
        if (drops.isEmpty()) {
            event.reply(CommandReaction.WARNING, "I could not find any item locations that matched your input!",
                    event::complete);
            return;
        }

        EmbedBuilder embed = event.createEmbedBuilder()
                .setTitle("Item Locations")
                .setFooter("warframestat.us", "https://warframestat.us/wfcd_logo_color.png");
        List<String> items = drops.stream().map(WfsDrop::getItem).distinct().collect(Collectors.toList());
        for (String item : items) {
            StringBuilder builder = new StringBuilder();
            drops.stream().filter(drop -> item.equals(drop.getItem())).distinct().forEach(drop -> builder
                    .append(drop.getPlace()).append(" - ").append(drop.getRarity()).append(" (")
                    .append(Formatter.formatPercent(drop.getChance() / 100)).append(")\n"));
            embed.addField(item, StringUtils.abbreviate(builder.toString(), MessageEmbed.VALUE_MAX_LENGTH), false);
        }
        try {
            event.reply(embed, event::complete);
        } catch (IllegalStateException e) {
            event.reply(CommandReaction.WARNING,
                    "Too many item locations matched your input! Please check the wiki instead.", event::complete);
        }
    }

}
