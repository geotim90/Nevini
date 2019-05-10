package de.nevini.resolvers;

import de.nevini.command.CommandEvent;
import de.nevini.util.Finder;
import lombok.NonNull;
import net.dv8tion.jda.core.Permission;

import java.util.List;
import java.util.regex.Pattern;

public class PermissionResolver extends AbstractResolver<Permission> {

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
