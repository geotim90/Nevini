package de.nevini.resolvers.osu;

import com.jagrosh.jdautilities.commons.utils.FinderUtil;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.OptionResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.util.command.CommandOptionDescriptor;
import lombok.NonNull;
import lombok.Value;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OsuUserResolver extends OptionResolver<OsuUserResolver.OsuUserOrMember> {

    @Value
    public static class OsuUserOrMember {
        private final String osuUser;
        private final User user;
        private final Member member;

        public OsuUserOrMember(@NonNull Member member) {
            this.member = member;
            this.user = member.getUser();
            this.osuUser = null;
        }

        public OsuUserOrMember(@NonNull User user) {
            this.member = null;
            this.user = user;
            this.osuUser = user.getName();
        }

        public OsuUserOrMember(@NonNull String osuUser) {
            this.member = null;
            this.user = null;
            this.osuUser = osuUser;
        }
    }

    OsuUserResolver() {
        super("user", new Pattern[]{
                FinderUtil.USER_MENTION,
                Pattern.compile("(?i)(?:(?:--|//)(?:user|member)|[-/][um])(?:\\s+(.+))?")
        });
    }

    @Override
    public CommandOptionDescriptor describe(boolean list, boolean argument) {
        return CommandOptionDescriptor.builder()
                .syntax("[--user] [<user>]")
                .description("Refers to " + (list ? "all osu! users" : "a specific osu! user")
                        + " with a matching mention, id, name, nickname or in-game name.\n"
                        + "Only supports osu! user ids and names when used via direct message.\n"
                        + "The `--user` flag is optional if a user mention is used"
                        + (argument ? " or this option is provided first" : "") + ".\n"
                        + "Refers to the current user if only the `--user` flag is provided.")
                .keyword("--user")
                .aliases(new String[]{"//user", "--member", "//member", "-u", "/u", "-m", "/m"})
                .build();
    }

    @Override
    public List<OsuUserOrMember> findSorted(@NonNull CommandEvent event, String query) {
        // check for matching members first
        if (event.isFromType(ChannelType.TEXT)) {
            List<OsuUserOrMember> members = Resolvers.MEMBER.findSorted(event, query).stream()
                    .map(OsuUserOrMember::new).collect(Collectors.toList());
            if (!members.isEmpty()) {
                return members;
            }
        }
        // otherwise accept query as a potential osu! user
        return Collections.singletonList(new OsuUserOrMember(query));
    }

    @Override
    protected @NonNull String getFieldNameForPicker(OsuUserOrMember item) {
        // should only be called for ambiguous member inputs
        return item.getMember().getEffectiveName();
    }

    @Override
    protected @NonNull String getFieldValueForPicker(OsuUserOrMember item) {
        // should only be called for ambiguous member inputs
        return item.getMember().getUser().getId();
    }

}
