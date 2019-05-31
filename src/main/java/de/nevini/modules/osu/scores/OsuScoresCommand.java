package de.nevini.modules.osu.scores;

import com.oopsjpeg.osu4j.GameMod;
import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuBeatmap;
import com.oopsjpeg.osu4j.OsuScore;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.resolvers.external.OsuBeatmapResolver;
import de.nevini.resolvers.external.OsuModeResolver;
import de.nevini.resolvers.external.OsuModsResolver;
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
public class OsuScoresCommand extends Command {

    private static final OsuModeResolver modeResolver = new OsuModeResolver();
    private static final OsuModsResolver modsResolver = new OsuModsResolver();

    private final OsuBeatmapResolver beatmapIdResolver;
    private final OsuService osu;

    public OsuScoresCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!scores")
                .node(Node.OSU_SCORES)
                .description("displays the top 100 scores of an osu! beatmap")
                .options(new CommandOptionDescriptor[]{
                        new OsuBeatmapResolver(osu).describe(true, false),
                        Resolvers.MEMBER.describe(false, false),
                        modeResolver.describe(false, false),
                        modsResolver.describe(false, false)
                })
                .build());
        this.beatmapIdResolver = new OsuBeatmapResolver(osu);
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        beatmapIdResolver.resolveArgumentOrOptionOrInput(event, beatmap -> acceptBeatmap(event, beatmap));
    }

    private void acceptBeatmap(CommandEvent event, OsuBeatmap beatmap) {
        Resolvers.MEMBER.resolveOptionOrDefaultIfExists(event, event.getMember(), member ->
                acceptBeatmapAndMember(event, beatmap, member));
    }

    private void acceptBeatmapAndMember(CommandEvent event, OsuBeatmap beatmap, Member member) {
        modeResolver.resolveOptionOrInputIfExists(event, mode ->
                acceptBeatmapAndMemberAndMode(event, beatmap, member, mode));
    }

    private void acceptBeatmapAndMemberAndMode(CommandEvent event, OsuBeatmap beatmap, Member member, GameMode mode) {
        modsResolver.resolveOptionOrInputIfExists(event, mods ->
                acceptBeatmapAndMemberAndModeAndMods(event, beatmap, member, mode, mods));
    }

    private void acceptBeatmapAndMemberAndModeAndMods(
            CommandEvent event, OsuBeatmap beatmap, Member member, GameMode mode, GameMod[] mods
    ) {
        GameData game = osu.getGame();
        String ign = member != null
                ? StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName())
                : null;
        List<OsuScore> scores = osu.getScores(beatmap.getID(), ign, mode, mods);
        if (scores == null || scores.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            embed.setTitle(beatmap.getTitle(), "https://osu.ppy.sh/b/" + beatmap.getID());
            for (OsuScore score : scores) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger(score.getScore()) + " - "
                                + Formatter.formatLargestUnitAgo(score.getDate()),
                        "[" + osu.getUserName(score.getUserID()) + "](https://osu.ppy.sh/u/"
                                + score.getUserID() + ")",
                        false);
            }
            event.reply(embed, event::complete);
        }
    }

}
