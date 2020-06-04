package de.nevini.resolvers.warframe;

import de.nevini.api.wfs.model.WfsRiven;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.services.warframe.WarframeStatsService;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfsRivenNameResolver extends OptionResolver<WfsRiven> {

    private Collection<WfsRiven> rivensCache = Collections.emptyList();

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
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        synchronized (this) {
            rivensCache = ObjectUtils.defaultIfNull(service.getRivens(), rivensCache);
        }
        return Finder.findAnyLenient(rivensCache, item -> new String[]{
                item.getDisplayName()
        }, query).stream().sorted(Comparator.comparing(WfsRiven::getDisplayName)).collect(Collectors.toList());
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
