package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.modules.Module;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModuleResolver extends AbstractResolver<Module> {

    public ModuleResolver() {
        super("module", new Pattern[]{Pattern.compile("(?i)(?:--|//)module(?:\\s+(.+))")});
    }

    @Override
    protected List<Module> findSorted(@NonNull CommandEvent event, String query) {
        return event.getModuleService().findModules(query).stream()
                .sorted(Comparator.comparing(Module::ordinal)).collect(Collectors.toList());
    }

}
