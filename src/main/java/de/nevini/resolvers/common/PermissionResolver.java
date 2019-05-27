package de.nevini.resolvers.common;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.Finder;
import net.dv8tion.jda.core.Permission;

import java.util.List;
import java.util.regex.Pattern;

public class PermissionResolver extends AbstractResolver<Permission> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("--permission <permission>")
                .description("Refers to a specific permission (e.g. \"Manage Server\").")
                .keyword("--permission")
                .aliases(new String[]{"//permission", "--perm", "//perm", "-p", "/p"});
    }

    protected PermissionResolver() {
        super("permission", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)(?:permission|perm)|[-/]p)(?:\\s+(.+))?")});
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
