package de.nevini.modules.warframe.resolvers;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.modules.warframe.api.wfs.model.weapons.WfsWeapon;
import de.nevini.modules.warframe.services.WarframeStatusService;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class WfsWeaponNameResolver extends OptionResolver<WfsWeapon> {

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
        WarframeStatusService service = event.locate(WarframeStatusService.class);
        return Finder.findLenient(service.getWeapons(), WfsWeapon::getName, query).stream()
                .sorted(Comparator.comparing(WfsWeapon::getName)).collect(Collectors.toList());
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
