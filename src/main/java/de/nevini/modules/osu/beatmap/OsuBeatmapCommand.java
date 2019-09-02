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
                .aliases(new String[]{"osu!b", "osu!beatmaps", "osu!beatmapset", "osu!bm", "osu!bms", "osu!filter",
                        "osu!find", "osu!map", "osu!maps", "osu!mapset", "osu!s", "osu!search", "osu!set"})
                .guildOnly(false)
                .node(Node.OSU_BEATMAP)
                .description("displays general information of osu! beatmaps")
                .options(new CommandOptionDescriptor[]{
                        OsuResolvers.BEATMAP.describe(true, true)
                })
                .details("This command supports search criteria similar to the in-game search function "
                        + "(<https://osu.ppy.sh/help/wiki/Interface#search>):"
                        + "\n* `b` - Beatmap id"
                        + "\n* `s` - Beatmapset id"
                        + "\n* `artist` - Song artist"
                        + "\n* `title` - Song title"
                        + "\n* `diff` - Beatmap difficulty name / version"
                        + "\n* `mode` - Mode - value can be `osu`, `taiko`, `catchthebeat`, or `mania`, or `o/t/c/m` for short"
                        + "\n* `mapper` - Mapper name"
                        + "\n* `status` - Ranked status - value can be `ranked`, `approved`, `pending`, `notsubmitted`, `unknown`, or `loved`, or `r/a/p/n/u/l` for short"
                        + "\n* `stars` - Star Difficulty"
                        + "\n* `length` - Length in seconds"
                        + "\n* `drain` - Drain Time in seconds"
                        + "\n* `bpm` - Beats per minute"
                        + "\n* `combo` - Max combo"
                        + "\n* `pp` - Max performance (without mods)"
                        + "\n* `circles` - Number of Circles"
                        + "\n* `spinners` - Number of Spinners"
                        + "\n* `sliders` - Number of Sliders"
                        + "\n* `cs` - Circle Size (CS)"
                        + "\n* `hp` - HP Drain Rate (HP)"
                        + "\n* `od` - Overall Difficulty (OD)"
                        + "\n* `ar` - Approach Rate (AR)"
                        + "\n* `aim` - Aim Difficulty"
                        + "\n* `speed` - Speed Difficulty"
                        + "\n* `rating` - Beatmapset rating"
                        + "\n* `source` - Song source"
                        + "\n* `genre` - Song genre"
                        + "\n* `language` - Song language"
                        + "\n* `tag` - Beatmapset tags")
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
                        embed.setTitle(Formatter.formatOsuBeatmap(beatmap), "https://osu.ppy.sh/b/" + beatmap.getBeatmapId());
                        embed.setDescription("Mapped by " + beatmap.getCreatorName());
                        embed.addField("Status", beatmap.getApproved().getName(), true);
                        embed.addField("Star Difficulty", Formatter.formatDouble(beatmap.getDifficultyRating()), true);
                        embed.addField("Length", Formatter.formatSeconds(beatmap.getTotalLength()), true);
                        embed.addField("Drain Length", Formatter.formatSeconds(beatmap.getHitLength()), true);
                        embed.addField("BPM", Formatter.formatDouble(beatmap.getBpm()), true);
                        if (beatmap.getMaxCombo() != null) {
                            if (beatmap.getMaxPp() != null) {
                                embed.addField("Max Combo", Formatter.formatInteger(beatmap.getMaxCombo()) + "x ≈ "
                                        + Formatter.formatInteger((int) Math.floor(beatmap.getMaxPp())) + "pp", true);
                            } else {
                                embed.addField("Max Combo", Formatter.formatInteger(beatmap.getMaxCombo()) + 'x', true);
                            }
                        }
                        embed.addField("Circles", Formatter.formatInteger(beatmap.getCountNormal()), true);
                        embed.addField("Sliders", Formatter.formatInteger(beatmap.getCountSlider()), true);
                        embed.addField("Spinners", Formatter.formatInteger(beatmap.getCountSpinner()), true);
                        embed.addField("Circle Size (CS)", Formatter.formatDouble(beatmap.getDifficultySize()), true);
                        embed.addField("HP Drain (HP)", Formatter.formatDouble(beatmap.getDifficultyDrain()), true);
                        embed.addField("Overall Difficulty (OD)", Formatter.formatDouble(beatmap.getDifficultyOverall()), true);
                        embed.addField("Approach Rate (AR)", Formatter.formatDouble(beatmap.getDifficultyApproach()), true);
                        if (beatmap.getDifficultyAim() != null) {
                            embed.addField("Aim Difficulty", Formatter.formatDouble(beatmap.getDifficultyAim()), true);
                        }
                        if (beatmap.getDifficultySpeed() != null) {
                            embed.addField("Speed Difficulty", Formatter.formatDouble(beatmap.getDifficultySpeed()), true);
                        }
                        embed.addField("Success Rate", Formatter.formatPercent((double) beatmap.getPassCount() / (double) beatmap.getPlayCount()), true);
                        embed.addField("Play Count", Formatter.formatInteger(beatmap.getPlayCount()), true);
                        embed.addField("Favourite Count", Formatter.formatInteger(beatmap.getFavouriteCount()), true);
                        embed.addField("User Rating", Formatter.formatDouble(beatmap.getRating()), true);
                        if (beatmap.getSubmitDate() != null) {
                            embed.addField("Submitted", Formatter.formatDate(beatmap.getSubmitDate()), true);
                        }
                        if (beatmap.getApprovedDate() != null) {
                            embed.addField("Ranked", Formatter.formatDate(beatmap.getApprovedDate()), true);
                        }
                        if (StringUtils.isNotEmpty(beatmap.getSource())) {
                            embed.addField("Source", beatmap.getSource(), true);
                        }
                        embed.addField("Genre", beatmap.getGenre().getName(), true);
                        embed.addField("Language", beatmap.getLanguage().getName(), true);
                        embed.addField("Tags", beatmap.getTags(), true);
                        return embed;
                    },
                    event.getEventDispatcher(),
                    event::complete
            ).display();
        }
    }

}
