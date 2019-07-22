package de.nevini.modules.osu.leaderboard;

import de.nevini.api.osu.model.OsuUser;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.scope.Node;
import de.nevini.services.osu.OsuService;
import de.nevini.util.Formatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OsuLeaderboardCommand extends Command {

    private static final String[] EMOTE_NUMBER = {
            "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3",
            "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3", "\uD83D\uDD1F"
    };

    public OsuLeaderboardCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!leaderboard")
                .aliases(new String[]{"osu!rank", "osu!ranks"})
                .node(Node.OSU_LEADERBOARD)
                .description("displays the top 10 osu! users on this server")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        event.notifyLongTaskStart();
        List<OsuUser> rankedUsers = event.getGuild().getMembers().stream().filter(member -> !member.getUser().isBot())
                .map(member -> {
                    String ign = event.getIgnService().getInGameName(member, game);
                    return StringUtils.isEmpty(ign) ? null : osuService.getUser(ign);
                })
                .filter(user -> user != null && user.getPpRank() > 0).sorted(Comparator.comparing(OsuUser::getPpRank))
                .limit(10).collect(Collectors.toList());
        event.notifyLongTaskEnd();
        if (rankedUsers.isEmpty()) {
            event.reply("No users found.", event::complete);
        } else {
            StringBuilder builder = new StringBuilder();
            int length = rankedUsers.size();
            for (int i = 0; i < length; i++) {
                OsuUser user = rankedUsers.get(i);
                builder.append(EMOTE_NUMBER[i]).append(" - **").append(user.getUserName()).append("** - #")
                        .append(Formatter.formatInteger(user.getPpRank())).append(" - ")
                        .append(Formatter.formatFloat(user.getAccuracy())).append("% - ")
                        .append(Formatter.formatInteger((int) Math.floor(user.getPpRaw()))).append("pp\n");
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}
