package de.nevini.modules.osu.leaderboard;

import com.oopsjpeg.osu4j.OsuUser;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.db.game.GameData;
import de.nevini.scope.Node;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OsuLeaderboardCommand extends Command {

    private static final String[] EMOTE_NUMBER = {
            "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3",
            "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3", "\uD83D\uDD1F"
    };

    private final OsuService osu;

    public OsuLeaderboardCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!leaderboard")
                .aliases(new String[]{"osu!rank", "osu!ranks"})
                .node(Node.OSU_LEADERBOARD)
                .description("displays the top 10 osu! users on this server")
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        GameData game = osu.getGame();
        List<OsuUser> rankedUsers = event.getGuild().getMembers().stream().filter(member -> !member.getUser().isBot())
                .map(member -> osu.getUser(StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName())))
                .filter(Objects::nonNull).sorted(Comparator.comparing(OsuUser::getRank)).limit(10).collect(Collectors.toList());
        if (rankedUsers.isEmpty()) {
            event.reply("No users found.", event::complete);
        } else {
            StringBuilder builder = new StringBuilder();
            int length = rankedUsers.size();
            for (int i = 0; i < length; i++) {
                OsuUser user = rankedUsers.get(i);
                builder.append(EMOTE_NUMBER[i]).append(" - **").append(user.getUsername()).append("** - #")
                        .append(Formatter.formatInteger(user.getRank())).append(" - ").append(Formatter.formatFloat(user.getAccuracy()))
                        .append("% - ").append(Formatter.formatInteger(user.getPP())).append("pp\n");
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}
