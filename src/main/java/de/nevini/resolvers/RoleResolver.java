package de.nevini.resolvers;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Role;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RoleResolver extends AbstractResolver<Role> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("[--role] <role>")
                .description("Refers to a specific role. The flag is optional if a role mention is used.")
                .keyword("--role")
                .aliases(new String[]{"//role", "-r", "/r"});
    }

    public RoleResolver() {
        super("role", new Pattern[]{
                Pattern.compile("<@&(\\d+)>"),
                Pattern.compile("(?i)(?:(?:--|//)role|[-/]r)(?:\\s+(.+))?")
        });
    }

    @Override
    public List<Role> findSorted(@NonNull CommandEvent event, String query) {
        return FinderUtil.findRoles(query, event.getGuild()).stream()
                .sorted(Comparator.comparing(Role::getName))
                .collect(Collectors.toList());
    }

    @Override
    protected String getFieldNameForPicker(Role item) {
        return item.getName();
    }

    @Override
    protected String getFieldValueForPicker(Role item) {
        return item.getId();
    }

}
