package de.nevini.resolvers.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.resolvers.AbstractResolver;
import lombok.NonNull;
import net.dv8tion.jda.core.entities.Member;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MemberResolver extends AbstractResolver<Member> {

    public static CommandOptionDescriptor.CommandOptionDescriptorBuilder describe() {
        return CommandOptionDescriptor.builder()
                .syntax("[--user] [<user>]")
                .description("Refers to a specific user using a mention, id or name."
                        + " The `--user` flag is optional if a user mention is used."
                        + " Refers to the current user if only the `--user` flag is provided.")
                .keyword("--user")
                .aliases(new String[]{"//user", "--member", "//member", "-u", "/u", "-m", "/m"});
    }

    protected MemberResolver() {
        super("user", new Pattern[]{
                Pattern.compile("<@!?(\\d+)>"),
                Pattern.compile("(?i)(?:(?:--|//)(?:user|member)|[-/][um])(?:\\s+(.+))?")
        });
    }

    @Override
    public List<Member> findSorted(@NonNull CommandEvent event, String query) {
        List<Member> matches = FinderUtil.findMembers(query, event.getGuild()).stream()
                .filter(m -> !m.getUser().isBot())
                .sorted(Comparator.comparing(Member::getEffectiveName))
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            matches = event.getIgnService().findByIgn(event.getGuild(), query).stream()
                    .map(data -> event.getGuild().getMemberById(data.getUser()))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(Member::getEffectiveName))
                    .distinct()
                    .collect(Collectors.toList());
        }

        if (matches.isEmpty() && (query.startsWith("@") || query.startsWith("!"))) {
            return findSorted(event, query.substring(1));
        } else {
            return matches;
        }
    }

    @Override
    protected String getFieldNameForPicker(Member item) {
        return item.getEffectiveName();
    }

    @Override
    protected String getFieldValueForPicker(Member item) {
        return item.getUser().getId();
    }

}
