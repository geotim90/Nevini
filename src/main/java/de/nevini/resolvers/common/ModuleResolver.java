package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.scope.Module;
import de.nevini.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class ModuleResolver extends AbstractResolver<Module> {

    protected ModuleResolver() {
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
    public List<Module> findSorted(CommandEvent ignore, String query) {
        return Finder.find(Module.values(), Module::getName, query);
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
