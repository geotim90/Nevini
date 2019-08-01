package de.nevini.resolvers.common;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.AbstractResolver;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import net.dv8tion.jda.api.entities.Member;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MemberResolver extends AbstractResolver<Member> {

    MemberResolver() {
        super("user", new Pattern[]{
                FinderUtil.USER_MENTION,
                Pattern.compile("(?i)(?:(?:--|//)(?:user|member)|[-/][um])(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax("[--user] [<user>]")
                .description("Refers to " + (list ? "all users" : "a specific user")
                        + " with a matching mention, id, name, nickname or in-game name.\n"
                        + "The `--user` flag is optional if a user mention is used"
                        + (argument ? " or this option is provided first" : "") + ".\n"
                        + "Refers to the current user if only the `--user` flag is provided.")
                .keyword("--user")
                .aliases(new String[]{"//user", "--member", "//member", "-u", "/u", "-m", "/m"})
                .build();
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
    protected @NonNull String getFieldNameForPicker(Member item) {
        return item.getEffectiveName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(Member item) {
        return item.getUser().getId();
    }

}
