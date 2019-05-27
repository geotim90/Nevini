package de.nevini.modules.osu.recent;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuScore;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.common.MemberResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.resolvers.external.OsuModeResolver;
import de.nevini.scope.Node;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuRecentCommand extends Command {

    private static final OsuModeResolver modeResolver = new OsuModeResolver();

    private final OsuService osu;

    public OsuRecentCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!recent")
                .aliases(new String[]{"osu!recents"})
                .children(new Command[]{
                        new OsuRecentFeedCommand()
                })
                .node(Node.OSU_RECENT)
                .description("displays up to 50 most recent plays over the last 24 hours of an osu! user")
                .options(new CommandOptionDescriptor[]{
                        MemberResolver.describe().build(),
                        OsuModeResolver.describe().build()
                })
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefault(event, event.getMember(), member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        modeResolver.resolveOptionOrInputIfExists(event, mode -> acceptMemberAndMode(event, member, mode));
    }

    private void acceptMemberAndMode(CommandEvent event, Member member, GameMode mode) {
        GameData game = osu.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        List<OsuScore> scores = osu.getUserRecent(ign, mode);
        if (scores == null || scores.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            int userId = scores.get(0).getUserID();
            String userName = osu.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuScore score : scores) {
                embed.addField(score.getRank() + " - " + Formatter.formatInteger(score.getScore()) + " - "
                                + Formatter.formatLargestUnitAgo(score.getDate()),
                        "[" + osu.getBeatmapTitle(score.getBeatmapID()) + "](https://osu.ppy.sh/b/"
                                + score.getBeatmapID() + ")",
                        false);
            }
            event.reply(embed, event::complete);
        }
    }

}
