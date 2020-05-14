package de.nevini.resolvers.warframe;

import de.nevini.api.wfs.model.WfsRiven;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfsRivenTypeResolver extends OptionResolver<WfsRiven> {

    WfsRivenTypeResolver() {
        super("riven type", new Pattern[]{
                Pattern.compile("(?i)(?:--|//)riven(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--riven] <type>" : "--riven <type>")
                .description("Refers to " + (list ? "all riven mods" : "a type of riven mod") + " with a matching name."
                        + (argument ? "\nThe `--riven` flag is optional if this option is provided first." : ""))
                .keyword("--riven")
                .aliases(new String[]{"//riven"})
                .build();
    }

    @Override
    public List<WfsRiven> findSorted(@NonNull CommandEvent event, String query) {
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        return Finder.findAnyLenient(service.getRivens(), item -> new String[]{
                item.getDisplayName(),
                item.getItemType(),
                item.getCompatibility()
        }, query).stream().sorted(Comparator.comparing(WfsRiven::getDisplayName)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(WfsRiven item) {
        return item.getDisplayName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(WfsRiven item) {
        return item.getItemType();
    }

}
