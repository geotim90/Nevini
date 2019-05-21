package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.util.Finder;
import lombok.NonNull;
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

    public PermissionResolver() {
        super("permission", new Pattern[]{Pattern.compile("(?i)(?:(?:--|//)(?:permission|perm)|[-/]p)(?:\\s+(.+))?")});
    }

    @Override
    protected List<Permission> findSorted(@NonNull CommandEvent event, String query) {
        return Finder.find(Permission.values(), Permission::getName, query);
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
