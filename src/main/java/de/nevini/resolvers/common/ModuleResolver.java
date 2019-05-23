package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.modules.Module;
import de.nevini.resolvers.AbstractResolver;
import lombok.NonNull;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModuleResolver extends AbstractResolver<Module> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--module <module>")
                .description("Refers to a specific bot module.")
                .keyword("--module")
                .aliases(new String[]{"//module"});
    }

    public ModuleResolver() {
        super("module", new Pattern[]{Pattern.compile("(?i)(?:--|//)module(?:\\s+(.+))?")});
    }

    @Override
    public List<Module> findSorted(@NonNull CommandEvent event, String query) {
        return event.getModuleService().findModules(query).stream()
                .sorted(Comparator.comparing(Module::ordinal)).collect(Collectors.toList());
    }

    @Override
    protected String getFieldNameForPicker(Module item) {
        return item.getName();
    }

    @Override
    protected String getFieldValueForPicker(Module item) {
        return "";
    }

}
