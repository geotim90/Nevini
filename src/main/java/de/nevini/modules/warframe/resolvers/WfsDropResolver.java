package de.nevini.modules.warframe.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.warframe.api.wfs.model.drops.WfsDrop;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.Finder;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfsDropResolver extends OptionResolver<WfsDrop> {

    WfsDropResolver() {
        super("item name", new Pattern[]{
                Pattern.compile("(?i)(?:--|//)item(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--item] <name>" : "--item <name>")
                .description("Refers to " + (list ? "all items" : "an item") + " with a matching name."
                        + (argument ? "\nThe `--item` flag is optional if this option is provided first." : ""))
                .keyword("--item")
                .aliases(new String[]{"//item"})
                .build();
    }

    @Override
    public List<WfsDrop> findSorted(@NonNull CommandEvent event, String query) {
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        return Finder.findLenient(service.getDrops(), WfsDrop::getItem, query).stream()
                .sorted(Comparator.comparing(WfsDrop::getItem)
                        .thenComparing(e -> -e.getChance())
                        .thenComparing(WfsDrop::getPlace))
                .collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(WfsDrop drop) {
        return drop.getItem();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(WfsDrop drop) {
        return drop.getPlace() + " - " + drop.getRarity() + " (" + Formatter.formatPercent(drop.getChance() / 100) + ")";
    }

}
