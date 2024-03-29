package de.nevini.core.resolvers.common;

import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.OptionResolver;
import de.nevini.core.scope.Module;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;

import java.util.List;
import java.util.regex.Pattern;

public class ModuleResolver extends OptionResolver<Module> {

    ModuleResolver() {
        super("module", new Pattern[]{Pattern.compile("(?i)(?:--|//)module(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--module] <module>" : "--module <module>")
                .description("Refers to " + (list ? "all bot modules" : "a specific bot module")
                        + " with a matching name."
                        + (argument
                        ? "\nThe `--module` flag is optional if this option is provided first."
                        : "")
                )
                .keyword("--module")
                .aliases(new String[]{"//module"})
                .build();
    }

    @Override
    public List<Module> findSorted(@NonNull CommandEvent ignore, String query) {
        return Finder.find(Module.values(), Module::getName, query);
    }

    @Override
    protected @NonNull String getFieldNameForPicker(Module item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(Module item) {
        return "";
    }

}
