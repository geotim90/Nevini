package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import net.dv8tion.jda.core.Permission;

import java.util.List;
import java.util.regex.Pattern;

public class PermissionResolver extends AbstractResolver<Permission> {

    PermissionResolver() {
        super("permission", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)(?:permission|perm)|[-/]p)(?:\\s+(.+))?")});
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax(argument ? "[--permission] <permission>" : "--permission <permission>")
                .description("Refers to " + (list ? "all permissions" : "a specific permission")
                        + " with a matching name (e.g. \"Manage Server\")."
                        + (argument
                        ? "\nThe `--permission` flag is optional if this option is provided first."
                        : ""))
                .keyword("--permission")
                .aliases(new String[]{"//permission", "--perm", "//perm", "-p", "/p"})
                .build();
    }

    @Override
    public List<Permission> findSorted(@NonNull CommandEvent ignore, String query) {
        return Finder.findAny(Permission.values(), p -> new String[]{p.getName(), p.name(),
                p.name().replace('_', ' ')}, query);
    }

    @Override
    protected @NonNull String getFieldNameForPicker(Permission item) {
        return item.getName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(Permission item) {
        return "";
    }

}
