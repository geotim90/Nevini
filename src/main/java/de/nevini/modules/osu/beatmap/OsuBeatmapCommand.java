package de.nevini.modules.osu.beatmap;

import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.resolvers.osu.OsuResolvers;
import de.nevini.scope.Node;
import de.nevini.services.osu.OsuService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.message.LazyMultiEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuBeatmapCommand extends Command {

    public OsuBeatmapCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!beatmap")
                .aliases(new String[]{"osu!b", "osu!bm", "osu!map"})
                .guildOnly(false)
                .node(Node.OSU_BEATMAP)
                .description("displays general information of osu! beatmaps")
                .options(new CommandOptionDescriptor[]{
                        OsuResolvers.BEATMAP.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        OsuResolvers.BEATMAP.resolveListArgumentOrOptionOrInput(event, beatmaps -> acceptBeatmap(event, beatmaps));
    }

    private void acceptBeatmap(CommandEvent event, List<OsuBeatmap> beatmaps) {
        if (beatmaps == null || beatmaps.isEmpty()) {
            event.reply("Beatmap not found.", event::complete);
        } else {
            OsuService osuService = event.locate(OsuService.class);
            GameData game = osuService.getGame();

            new LazyMultiEmbed<>(
                    event.getChannel(),
                    event.getAuthor(),
                    null,
                    beatmaps,
                    item -> {
                        // update information - beatmap information may be outdated
                        OsuBeatmap beatmap = osuService.getBeatmap(item.getBeatmapId());

                        EmbedBuilder embed = event.createEmbedBuilder();
                        embed.setAuthor(game.getName(), null, game.getIcon());
                        embed.setTitle(beatmap.getTitle(), "https://osu.ppy.sh/b/" + beatmap.getBeatmapId());
                        embed.setDescription(beatmap.getArtist());
                        embed.addField("Game Mode", beatmap.getMode().getName(), true);
                        embed.addField("Version", beatmap.getVersion(), true);
                        embed.addField("Star Difficulty", Formatter.formatFloat(beatmap.getDifficultyRating()), true);
                        embed.addField("Status", beatmap.getApproved().getName(), true);
                        embed.addField("Play Count", Formatter.formatInteger(beatmap.getPlayCount()), true);
                        embed.addField("Favourite Count", Formatter.formatInteger(beatmap.getFavouriteCount()), true);
                        embed.addField("Mapped", beatmap.getCreatorName(), true);
                        embed.addField("Submitted", Formatter.formatTimestamp(beatmap.getSubmitDate().getTime()), true);
                        embed.addField("Ranked", Formatter.formatTimestamp(beatmap.getApprovedDate().getTime()), true);
                        embed.addField("Length", Formatter.formatSeconds(beatmap.getTotalLength()), true);
                        embed.addField("Drain Length", Formatter.formatSeconds(beatmap.getHitLength()), true);
                        embed.addField("BPM", Formatter.formatFloat(beatmap.getBpm()), true);
                        embed.addField("Max Combo", Formatter.formatInteger(beatmap.getMaxCombo()), true);
                        embed.addField("Circle Size (CS)", Formatter.formatFloat(beatmap.getDifficultySize()), true);
                        embed.addField("HP Drain (HP)", Formatter.formatFloat(beatmap.getDifficultyDrain()), true);
                        embed.addField("Accuracy (OD)", Formatter.formatFloat(beatmap.getDifficultyOverall()), true);
                        embed.addField("Approach Rate (AR)", Formatter.formatFloat(beatmap.getDifficultyApproach()), true);
                        embed.addField("User Rating", Formatter.formatFloat(beatmap.getRating()), true);
                        embed.addField("Source", beatmap.getSource(), true);
                        embed.addField("Genre", beatmap.getGenre().getName(), true);
                        embed.addField("Language", beatmap.getLanguage().getName(), true);
                        embed.addField("Tags", StringUtils.join(beatmap.getTags(), ", "), true);
                        embed.addField("Success Rate", Formatter.formatPercent((double) beatmap.getPassCount() / (double) beatmap.getPlayCount()), true);
                        return embed;
                    },
                    event.getEventDispatcher(),
                    event::complete
            ).display();
        }
    }

}
