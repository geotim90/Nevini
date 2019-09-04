package de.nevini.modules.osu.diag;

import de.nevini.api.osu.model.OsuBeatmap;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUserBest;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.jpa.game.GameData;
import de.nevini.modules.osu.OsuCommandUtils;
import de.nevini.resolvers.osu.OsuResolvers;
import de.nevini.resolvers.osu.OsuUserResolver;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.services.osu.OsuService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OsuDiagCommand extends Command {

    public OsuDiagCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!diag")
                .guildOnly(false)
                .node(Node.OSU_BEST)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("analyses the top 100 scores of an osu! user")
                .options(new CommandOptionDescriptor[]{
                        OsuResolvers.USER.describe(false, true),
                        OsuResolvers.MODE.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        OsuResolvers.USER.resolveArgumentOrOptionOrDefault(event,
                OsuCommandUtils.getCurrentUserOrMember(event),
                userOrMember -> acceptUserOrMember(event, userOrMember));
    }

    private void acceptUserOrMember(CommandEvent event, OsuUserResolver.OsuUserOrMember userOrMember) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, userOrMember, mode));
    }

    private void acceptUserAndMode(CommandEvent event, OsuUserResolver.OsuUserOrMember userOrMember, OsuMode mode) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = OsuCommandUtils.resolveUserName(userOrMember, event.getIgnService(), game);
        List<OsuUserBest> best = osuService.getUserBest(ign, mode);
        if (best == null || best.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            event.notifyLongTaskStart();
            // collect data
            List<OsuBeatmap> beatmaps = best.stream().map(score -> osuService.getBeatmapCached(score.getBeatmapId()))
                    .collect(Collectors.toList());
            // output statistics
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            int userId = best.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            embed.addField("Score Statistics", "(weighted average, followed by the minimum, average and maximum values)", false);
            embed.addField("Score", formatIntegers(best, OsuUserBest::getScore), true);
            embed.addField("Max Combo", formatIntegers(best, OsuUserBest::getMaxCombo), true);
            embed.addField("Performance Points", formatDoubles(best, OsuUserBest::getPp), true);
            embed.addField("300s", formatIntegers(best, OsuUserBest::getCount300), true);
            embed.addField("100s", formatIntegers(best, OsuUserBest::getCount100), true);
            embed.addField("50s", formatIntegers(best, OsuUserBest::getCount50), true);
            embed.addField("Geki", formatIntegers(best, OsuUserBest::getCountGeki), true);
            embed.addField("Katu", formatIntegers(best, OsuUserBest::getCountKatu), true);
            embed.addField("Misses", formatIntegers(best, OsuUserBest::getCountMiss), true);
            embed.addField("Beatmap Statistics", "(weighted average, followed by the minimum, average and maximum values)", false);
            embed.addField("Star Difficulty", formatDoubles(beatmaps, OsuBeatmap::getDifficultyRating), true);
            embed.addField("Max Combo (x)", formatIntegers(beatmaps, OsuBeatmap::getMaxCombo), true);
            embed.addField("Max Combo (pp)", formatDoubles(beatmaps, OsuBeatmap::getMaxPp), true);
            embed.addField("Circle Size (CS)", formatDoubles(beatmaps, OsuBeatmap::getDifficultySize), true);
            embed.addField("HP Drain (HP)", formatDoubles(beatmaps, OsuBeatmap::getDifficultyDrain), true);
            embed.addField("Overall Difficulty (OD)", formatDoubles(beatmaps, OsuBeatmap::getDifficultyOverall), true);
            embed.addField("Approach Rate (AR)", formatDoubles(beatmaps, OsuBeatmap::getDifficultyApproach), true);
            embed.addField("Aim Difficulty", formatDoubles(beatmaps, OsuBeatmap::getDifficultyAim), true);
            embed.addField("Speed Difficulty", formatDoubles(beatmaps, OsuBeatmap::getDifficultySpeed), true);
            embed.addField("Beatmap Statistics", "(weighted average, followed by the minimum, average and maximum values)", false);
            embed.addField("Length", formatSeconds(beatmaps, OsuBeatmap::getTotalLength), true);
            embed.addField("Drain Length", formatSeconds(beatmaps, OsuBeatmap::getHitLength), true);
            embed.addField("BPM", formatDoubles(beatmaps, OsuBeatmap::getBpm), true);
            embed.addField("Circles", formatIntegers(beatmaps, OsuBeatmap::getCountNormal), true);
            embed.addField("Sliders", formatIntegers(beatmaps, OsuBeatmap::getCountSlider), true);
            embed.addField("Spinners", formatIntegers(beatmaps, OsuBeatmap::getCountSpinner), true);
            event.notifyLongTaskEnd();
            event.reply(embed, event::complete);
        }
    }

    // approx: e ^ (-0.05 * index)
    private static final double[] WEIGHT = {
            1, 0.95, 0.9, 0.86, 0.81, 0.77, 0.74, 0.7, 0.66, 0.63, 0.6, 0.57, 0.54, 0.51,
            0.49, 0.46, 0.44, 0.42, 0.4, 0.38, 0.36, 0.34, 0.32, 0.31, 0.29, 0.28, 0.26, 0.25,
            0.24, 0.23, 0.21, 0.2, 0.19, 0.18, 0.17, 0.17, 0.16, 0.15, 0.14, 0.14, 0.13,
            0.12, 0.12, 0.11, 0.1, 0.1, 0.09, 0.09, 0.09, 0.08, 0.08, 0.07, 0.07, 0.07,
            0.06, 0.06, 0.06, 0.05, 0.05, 0.05, 0.05, 0.04, 0.04, 0.04, 0.04, 0.04,
            0.03, 0.03, 0.03, 0.03, 0.03, 0.03, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02, 0.02,
            0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01, 0.01
    };

    private static <T> String formatDoubles(List<T> list, Function<T, Double> function) {
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        double weighted_sum = 0;
        double weights_sum = 0;
        double sum = 0;
        int count = 0;
        for (T e : list) {
            if (e != null) {
                Double value = function.apply(e);
                if (value != null) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                    weighted_sum += WEIGHT[count] * value;
                    weights_sum += WEIGHT[count];
                    sum += value;
                    count++;
                }
            }
        }
        if (count > 0) {
            return Formatter.formatDecimal(weighted_sum / weights_sum) + '\n' + Formatter.formatDecimal(min) + " - "
                    + Formatter.formatDecimal(sum / count) + " - " + Formatter.formatDecimal(max);
        } else {
            return "N/A";
        }
    }

    private static <T> String formatIntegers(List<T> list, Function<T, Integer> function) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        double weighted_sum = 0;
        double weights_sum = 0;
        int sum = 0;
        int count = 0;
        for (T e : list) {
            if (e != null) {
                Integer value = function.apply(e);
                if (value != null) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                    weighted_sum += WEIGHT[count] * value;
                    weights_sum += WEIGHT[count];
                    sum += value;
                    count++;
                }
            }
        }
        if (count > 0) {
            int weightedAverage = (int) Math.floor(weighted_sum / weights_sum);
            return Formatter.formatInteger(weightedAverage) + '\n' + Formatter.formatLargeInteger(min) + " - "
                    + Formatter.formatLargeInteger(sum / count) + " - " + Formatter.formatLargeInteger(max);
        } else {
            return "N/A";
        }
    }

    private static <T> String formatSeconds(List<T> list, Function<T, Integer> function) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        double weighted_sum = 0;
        double weights_sum = 0;
        int sum = 0;
        int count = 0;
        for (T e : list) {
            if (e != null) {
                Integer value = function.apply(e);
                if (value != null) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                    weighted_sum += WEIGHT[count] * value;
                    weights_sum += WEIGHT[count];
                    sum += value;
                    count++;
                }
            }
        }
        if (count > 0) {
            int weightedAverage = (int) Math.floor(weighted_sum / weights_sum);
            return Formatter.formatSeconds(weightedAverage) + '\n' + Formatter.formatSeconds(min) + " - "
                    + Formatter.formatSeconds(sum / count) + " - " + Formatter.formatSeconds(max);
        } else {
            return "N/A";
        }
    }

}
