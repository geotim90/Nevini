package de.nevini.resolvers.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Role;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RoleResolver extends AbstractResolver<Role> {

    RoleResolver() {
        super("role", new Pattern[]{
                FinderUtil.ROLE_MENTION,
                Pattern.compile("(?i)(?:(?:--|//)role|[-/]r)(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax("[--role] <role>")
                .description("Refers to " + (list ? "all roles" : "a specific role")
                        + " with a matching mention, id or name.\n"
                        + "The `--role` flag is optional if a channel mention is used"
                        + (argument ? " or this option is provided first" : "") + ".")
                .keyword("--role")
                .aliases(new String[]{"//role", "-r", "/r"})
                .build();
    }

    @Override
    public List<Role> findSorted(@NonNull CommandEvent event, String query) {
        List<Role> matches = FinderUtil.findRoles(query, event.getGuild()).stream()
                .sorted(Comparator.comparing(Role::getPosition).reversed())
                .collect(Collectors.toList());

        if (matches.isEmpty() && (query.startsWith("@") || query.startsWith("&"))) {
            return findSorted(event, query.substring(1));
        } else {
            return matches;
        }
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
