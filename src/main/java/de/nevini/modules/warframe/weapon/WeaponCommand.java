package de.nevini.modules.warframe.weapon;

import de.nevini.api.wfs.model.weapons.WfsWeapon;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.warframe.WarframeResolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WeaponCommand extends Command {

    public WeaponCommand() {
        super(CommandDescriptor.builder()
                .keyword("weapon")
                .guildOnly(false)
                .node(Node.WARFRAME)
                .description("displays weapon information using data from warframestat.us")
                .options(new CommandOptionDescriptor[]{
                        WarframeResolvers.WEAPON.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        WarframeResolvers.WEAPON.resolveArgumentOrOptionOrInput(event, item -> acceptWeapon(event, item));
    }

    private void acceptWeapon(CommandEvent event, WfsWeapon item) {
        EmbedBuilder embedBuilder = event.createEmbedBuilder()
                .setTitle(item.getName(), item.getWikiaUrl())
                .setDescription("MR " + item.getMasteryReq() + " " + item.getType() + "\n\n" + item.getDescription())
                .setThumbnail(item.getWikiaThumbnail())
                .setFooter("warframestat.us", "https://warframestat.us/wfcd_logo_color.png");
        if (item.getAccuracy() != null && item.getAccuracy() > 0) {
            embedBuilder.addField("Accuracy", Formatter.formatDecimal(item.getAccuracy()), true);
        }
        if (item.getCriticalChance() != null && item.getCriticalChance() > 0) {
            embedBuilder.addField("Critical Chance", Formatter.formatPercent(item.getCriticalChance()), true);
        }
        if (item.getCriticalMultiplier() != null && item.getCriticalMultiplier() > 0) {
            embedBuilder.addField("Critical Multiplier", Formatter.formatDecimal(item.getCriticalMultiplier()) + "x",
                    true);
        }
        if (item.getDamageTypes() != null && !item.getDamageTypes().isEmpty()) {
            embedBuilder.addField("Damage", item.getDamageTypes().entrySet().stream().map(
                    e -> Formatter.formatDecimal(e.getValue()) + " " + e.getKey()
            ).collect(Collectors.joining("\n")), true);
        }
        if (item.getDisposition() != null) {
            embedBuilder.addField("Disposition", item.getDisposition() + "/5", true);
        }
        if (item.getFireRate() != null && item.getFireRate() > 0) {
            embedBuilder.addField("Fire rate", Formatter.formatDecimal(item.getFireRate()) + "/s", true);
        }
        if (item.getMagazineSize() != null && item.getMagazineSize() > 0) {
            embedBuilder.addField("Magazine", Formatter.formatInteger(item.getMagazineSize()), true);
        }
        if (item.getMultishot() != null && item.getMultishot() > 1) {
            embedBuilder.addField("Multishot", Formatter.formatInteger(item.getMultishot()), true);
        }
        if (StringUtils.isNotEmpty(item.getNoise())) {
            embedBuilder.addField("Noise", item.getNoise(), true);
        }
        if (item.getPolarities() != null && item.getPolarities().length > 0) {
            embedBuilder.addField("Polarities", StringUtils.join(item.getPolarities(), "\n"), true);
        }
        if (item.getReloadTime() != null && item.getReloadTime() > 0) {
            embedBuilder.addField("Reload", Formatter.formatDecimal(item.getReloadTime()) + "s", true);
        }
        if (StringUtils.isNotEmpty(item.getStancePolarity())) {
            embedBuilder.addField("Stance Polarity", item.getStancePolarity(), true);
        }
        if (item.getProcChance() != null && item.getProcChance() > 0) {
            embedBuilder.addField("Status", Formatter.formatPercent(item.getProcChance()), true);
        }
        if (StringUtils.isNotEmpty(item.getTrigger())) {
            embedBuilder.addField("Trigger", item.getTrigger(), true);
        }
        event.reply(embedBuilder, event::complete);
    }

}
