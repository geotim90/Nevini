package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;
import net.dv8tion.jda.core.Permission;

import java.util.List;
import java.util.regex.Pattern;

public class PermissionResolver extends AbstractResolver<Permission> {

    protected PermissionResolver() {
        super("permission", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)(?:permission|perm)|[-/]p)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean resolvesArgument, boolean resolvesList) {
        return CommandOptionDescriptor.builder()
                .syntax(resolvesArgument ? "[--permission] <permission>" : "--permission <permission>")
                .description("Refers to " + (resolvesList ? "all permissions" : "a specific permission")
                        + " with a matching name (e.g. \"Manage Server\")."
                        + (resolvesArgument ? "\nThe `--permission` flag is optional if this option is provided first." : ""))
                .keyword("--feed")
                .aliases(new String[]{"//feed", "-f", "/f"})
                .build();
    }

    @Override
    public List<Permission> findSorted(CommandEvent ignore, String query) {
        return Finder.findAny(Permission.values(), p -> new String[]{p.getName(), p.name(), p.name().replace('_', ' ')}, query);
    }

    @Override
    protected String getFieldNameForPicker(Permission item) {
        return item.getName();
    }

    @Override
    protected String getFieldValueForPicker(Permission item) {
        return "";
    }

}
