package de.nevini.modules.osu.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.modules.osu.model.OsuBeatmap;
import de.nevini.modules.osu.model.OsuMod;
import de.nevini.modules.osu.model.OsuMode;
import de.nevini.modules.osu.model.OsuScore;
import de.nevini.modules.osu.resolvers.OsuResolvers;
import de.nevini.modules.osu.resolvers.OsuUserResolver;
import de.nevini.modules.osu.services.OsuService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuScoresCommand extends Command {

    public OsuScoresCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!scores")
                .guildOnly(false)
                .node(Node.OSU)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("displays the top 100 scores of an osu! beatmap")
                .options(new CommandOptionDescriptor[]{
                        OsuResolvers.BEATMAP.describe(false, true),
                        OsuResolvers.USER.describe(),
                        OsuResolvers.MODE.describe(),
                        OsuResolvers.MODS.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        OsuResolvers.BEATMAP.resolveArgumentOrOptionOrInput(event, beatmap -> acceptBeatmap(event, beatmap));
    }

    private void acceptBeatmap(CommandEvent event, OsuBeatmap beatmap) {
        OsuResolvers.USER.resolveOptionOrDefaultIfExists(event,
                OsuCommandUtils.getCurrentUserOrMember(event),
                userOrMember -> acceptBeatmapAndMember(event, beatmap, userOrMember));
    }

    private void acceptBeatmapAndMember(
            CommandEvent event, OsuBeatmap beatmap, OsuUserResolver.OsuUserOrMember userOrMember
    ) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode ->
                acceptBeatmapAndMemberAndMode(event, beatmap, userOrMember, mode));
    }

    private void acceptBeatmapAndMemberAndMode(
            CommandEvent event, OsuBeatmap beatmap, OsuUserResolver.OsuUserOrMember userOrMember, OsuMode mode
    ) {
        OsuResolvers.MODS.resolveOptionOrInputIfExists(event, mods ->
                acceptBeatmapAndMemberAndModeAndMods(event, beatmap, userOrMember, mode, mods));
    }

    private void acceptBeatmapAndMemberAndModeAndMods(
            CommandEvent event, OsuBeatmap beatmap, OsuUserResolver.OsuUserOrMember userOrMember,
            OsuMode mode, OsuMod[] mods
    ) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = userOrMember != null
                ? OsuCommandUtils.resolveUserName(userOrMember, event.getIgnService(), game)
                : null;
        List<OsuScore> scores = osuService.getScores(beatmap.getBeatmapId(), ign, mode, mods);
        if (scores == null || scores.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            event.notifyLongTaskStart();
            EmbedBuilder embed = event.createGameEmbedBuilder(game);
            embed.setTitle(beatmap.getTitle(), "https://osu.ppy.sh/b/" + beatmap.getBeatmapId());
            for (OsuScore score : scores) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger(score.getScore()) + " - "
                                + Formatter.formatLargestUnitAgo(score.getDate()),
                        "[" + osuService.getUserName(score.getUserId()) + "](https://osu.ppy.sh/u/"
                                + score.getUserId() + ")",
                        false);
            }
            event.notifyLongTaskEnd();
            event.reply(embed, event::complete);
        }
    }

}
