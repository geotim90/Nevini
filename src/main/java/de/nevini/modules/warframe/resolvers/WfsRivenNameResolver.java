package de.nevini.modules.warframe.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.warframe.api.wfs.model.rivens.WfsRiven;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfsRivenNameResolver extends OptionResolver<WfsRiven> {

    WfsRivenNameResolver() {
        super("riven name", new Pattern[]{
                Pattern.compile("(?i)(?:--|//)riven(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--riven] <weapon>" : "--riven <weapon>")
                .description("Refers to " + (list ? "all riven mods" : "a riven mod") + " for a matching weapon."
                        + (argument ? "\nThe `--riven` flag is optional if this option is provided first." : ""))
                .keyword("--riven")
                .aliases(new String[]{"//riven"})
                .build();
    }

    @Override
    public List<WfsRiven> findSorted(@NonNull CommandEvent event, String query) {
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        return Finder.findLenient(service.getRivens(), WfsRiven::getDisplayName, query).stream()
                .sorted(Comparator.comparing(WfsRiven::getDisplayName)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(WfsRiven item) {
        return item.getDisplayName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(WfsRiven item) {
        return "";
    }

}
