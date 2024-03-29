package de.nevini.modules.warframe.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.warframe.api.wfm.model.items.WfmItemName;
import de.nevini.modules.warframe.services.WarframeMarketService;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfmItemNameResolver extends OptionResolver<WfmItemName> {

    WfmItemNameResolver() {
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
    public List<WfmItemName> findSorted(@NonNull CommandEvent event, String query) {
        WarframeMarketService service = event.locate(WarframeMarketService.class);
        return Finder.findAnyLenient(service.getItemNames(), item -> new String[]{
                item.getItemName(),
                item.getItemName().replace(" Prime", ""),
                item.getUrlName().replace('_', ' '),
                item.getId()
        }, query).stream().sorted(Comparator.comparing(WfmItemName::getItemName)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(WfmItemName item) {
        return item.getItemName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(WfmItemName item) {
        return item.getId();
    }

}
