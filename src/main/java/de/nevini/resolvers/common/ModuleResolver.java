package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.scope.Module;
import de.nevini.util.Finder;

import java.util.List;
import java.util.regex.Pattern;

public class ModuleResolver extends AbstractResolver<Module> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--module <module>")
                .description("Refers to a specific bot module.")
                .keyword("--module")
                .aliases(new String[]{"//module"});
    }

    protected ModuleResolver() {
        super("module", new Pattern[]{Pattern.compile("(?i)(?:--|//)module(?:\\s+(.+))?")});
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
