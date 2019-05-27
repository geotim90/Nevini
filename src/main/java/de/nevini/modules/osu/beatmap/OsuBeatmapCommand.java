package de.nevini.modules.osu.beatmap;

import com.oopsjpeg.osu4j.OsuBeatmap;
import de.nevini.command.*;
import de.nevini.resolvers.external.OsuBeatmapIdResolver;
import de.nevini.resolvers.external.OsuModeResolver;
import de.nevini.scope.Node;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OsuBeatmapCommand extends Command {

    private final OsuService osu;

    public OsuBeatmapCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!beatmap")
                .aliases(new String[]{"osu!b", "osu!bm", "osu!map"})
                .node(Node.OSU_BEATMAP)
                .description("displays general information of an osu! beatmap")
                .options(new CommandOptionDescriptor[]{
                        OsuBeatmapIdResolver.describe().build(),
                        OsuModeResolver.describe().build()
                })
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        String input = event.getArgument();
        if (StringUtils.isEmpty(input) || !input.matches("\\d+")) {
            event.reply(CommandReaction.WARNING, "You did not provide a beatmap ID!", event::complete);
            return;
        }
        int beatmapId;
        try {
            beatmapId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            event.reply(CommandReaction.WARNING, "You did not provide a beatmap ID!", event::complete);
            return;
        }
        acceptBeatmapId(event, beatmapId);
    }

    private void acceptBeatmapId(CommandEvent event, int beatmapId) {
        OsuBeatmap beatmap = osu.getBeatmap(beatmapId);
        if (beatmap == null) {
            event.reply("Beatmap not found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            event.getGameService().findGames("osu!").stream().findFirst().ifPresent(
                    game -> embed.setAuthor(game.getName(), null, game.getIcon())
            );
            embed.setTitle(beatmap.getTitle(), "https://osu.ppy.sh/b/" + beatmap.getID());
            embed.setDescription(beatmap.getArtist());
            embed.addField("Game Mode", beatmap.getMode().getName(), true);
            embed.addField("Version", beatmap.getVersion(), true);
            embed.addField("Star Difficulty", Formatter.formatFloat(beatmap.getDifficulty()), true);
            embed.addField("Status", beatmap.getApproved().getName(), true);
            embed.addField("Play Count", Formatter.formatInteger(beatmap.getPlayCount()), true);
            embed.addField("Favourite Count", Formatter.formatInteger(beatmap.getFavouriteCount()), true);
            embed.addField("Mapped", beatmap.getCreatorName(), true);
            embed.addField("Submitted", Formatter.formatTimestamp(beatmap.getSubmitDate()), true);
            embed.addField("Ranked", Formatter.formatTimestamp(beatmap.getApprovedDate()), true);
            embed.addField("Length", Formatter.formatSeconds(beatmap.getTotalLength()), true);
            embed.addField("Drain Length", Formatter.formatSeconds(beatmap.getHitLength()), true);
            embed.addField("BPM", Formatter.formatFloat(beatmap.getBPM()), true);
            embed.addField("Max Combo", Formatter.formatInteger(beatmap.getMaxCombo()), true);
            embed.addField("Circle Size (CS)", Formatter.formatFloat(beatmap.getSize()), true);
            embed.addField("HP Drain (HP)", Formatter.formatFloat(beatmap.getDrain()), true);
            embed.addField("Accuracy (OD)", Formatter.formatFloat(beatmap.getOverall()), true);
            embed.addField("Approach Rate (AR)", Formatter.formatFloat(beatmap.getApproach()), true);
            embed.addField("User Rating", Formatter.formatFloat(beatmap.getRating()), true);
            embed.addField("Source", beatmap.getSource(), true);
            embed.addField("Genre", beatmap.getGenre().getName(), true);
            embed.addField("Language", beatmap.getLanguage().getName(), true);
            embed.addField("Tags", StringUtils.join(beatmap.getTags(), ", "), true);
            embed.addField("Success Rate", Formatter.formatPercent((double) beatmap.getPassCount() / (double) beatmap.getPlayCount()), true);
            event.reply(embed, event::complete);
        }
    }

}
