package de.nevini.modules.osu.best;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuScore;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.resolvers.external.OsuResolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuBestCommand extends Command {

    public OsuBestCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!best")
                .node(Node.OSU_BEST)
                .description("displays the top 100 scores of an osu! user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(true, false),
                        OsuResolvers.MODE.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefault(event, event.getMember(), member -> acceptUser(event, member));
    }

    private void acceptUser(CommandEvent event, Member member) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, member, mode));
    }

    private void acceptUserAndMode(CommandEvent event, Member member, GameMode mode) {
        GameData game = event.getOsuService().getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        List<OsuScore> scores = event.getOsuService().getUserBest(ign, mode);
        if (scores == null || scores.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            int userId = scores.get(0).getUserID();
            String userName = event.getOsuService().getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuScore score : scores) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger((int) Math.floor(score.getPp())) + "pp - "
                                + Formatter.formatLargestUnitAgo(score.getDate()),
                        "[" + event.getOsuService().getBeatmapString(score.getBeatmapID())
                                + "](https://osu.ppy.sh/b/" + score.getBeatmapID() + ")",
                        false);
            }
            event.reply(embed, event::complete);
        }
    }

}
