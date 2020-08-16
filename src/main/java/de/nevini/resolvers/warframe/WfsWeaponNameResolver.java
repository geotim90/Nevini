package de.nevini.resolvers.warframe;

import de.nevini.api.wfs.model.weapons.WfsWeapon;
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

public class WfsWeaponNameResolver extends OptionResolver<WfsWeapon> {

    private Collection<WfsWeapon> weaponsCache = Collections.emptyList();

    WfsWeaponNameResolver() {
        super("weapon name", new Pattern[]{
                Pattern.compile("(?i)(?:--|//)weapon(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--weapon] <name>" : "--weapon <name>")
                .description("Refers to " + (list ? "all weapons" : "a weapon") + " with a matching name."
                        + (argument ? "\nThe `--weapon` flag is optional if this option is provided first." : ""))
                .keyword("--weapon")
                .aliases(new String[]{"//weapon"})
                .build();
    }

    @Override
    public List<WfsWeapon> findSorted(@NonNull CommandEvent event, String query) {
        WarframeStatsService service = event.locate(WarframeStatsService.class);
        synchronized (this) {
            weaponsCache = ObjectUtils.defaultIfNull(service.getWeapons(), weaponsCache);
        }
        return Finder.findAnyLenient(weaponsCache, item -> new String[]{
                item.getName()
        }, query).stream().sorted(Comparator.comparing(WfsWeapon::getName)).collect(Collectors.toList());
    }

    @Override
    protected @NonNull String getFieldNameForPicker(WfsWeapon item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(WfsWeapon item) {
        return "";
    }

}
