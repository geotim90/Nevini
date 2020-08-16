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
            List<OsuBeatmap> beatmaps = best.stream().map(score -> osuService.getBeatmapCached(
                    score.getBeatmapId(), score.getMode(), score.getMods()
            )).collect(Collectors.toList());
            // calculate performance in percent (min=1%)
            double[] performance = new double[best.size()];
            for (int i = 0; i < performance.length; ++i) {
                OsuBeatmap beatmap = beatmaps.get(i);
                if (beatmap != null) {
                    Double maxPp = beatmap.getMaxPp();
                    if (maxPp != null && maxPp != 0) {
                        performance[i] = Math.max(best.get(i).getPp() / maxPp, 0.01);
                    } else {
                        performance[i] = 0.01;
                    }
                }
            }
            // output statistics
            EmbedBuilder embed = event.createGameEmbedBuilder(game);
            int userId = best.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            embed.addField("Score Statistics", "(weighted average, followed by the minimum, average and maximum values)", false);
            embed.addField("Score", formatIntegers(best, OsuUserBest::getScore, performance), true);
            embed.addField("Max Combo", formatIntegers(best, OsuUserBest::getMaxCombo, performance), true);
            embed.addField("Performance Points", formatDoubles(best, OsuUserBest::getPp, performance), true);
            embed.addField("300s", formatIntegers(best, OsuUserBest::getCount300, performance), true);
            embed.addField("100s", formatIntegers(best, OsuUserBest::getCount100, performance), true);
            embed.addField("50s", formatIntegers(best, OsuUserBest::getCount50, performance), true);
            embed.addField("Geki", formatIntegers(best, OsuUserBest::getCountGeki, performance), true);
            embed.addField("Katu", formatIntegers(best, OsuUserBest::getCountKatu, performance), true);
            embed.addField("Misses", formatIntegers(best, OsuUserBest::getCountMiss, performance), true);
            embed.addField("Beatmap Statistics", "(weighted average, followed by the minimum, average and maximum values)", false);
            embed.addField("Star Difficulty", formatDoubles(beatmaps, OsuBeatmap::getDifficultyRating, performance), true);
            embed.addField("Max Combo (x)", formatIntegers(beatmaps, OsuBeatmap::getMaxCombo, performance), true);
            embed.addField("Max Combo (pp)", formatDoubles(beatmaps, OsuBeatmap::getMaxPp, performance), true);
            embed.addField("Circle Size (CS)", formatDoubles(beatmaps, OsuBeatmap::getDifficultySize, performance), true);
            embed.addField("HP Drain (HP)", formatDoubles(beatmaps, OsuBeatmap::getDifficultyDrain, performance), true);
            embed.addField("Overall Difficulty (OD)", formatDoubles(beatmaps, OsuBeatmap::getDifficultyOverall, performance), true);
            embed.addField("Approach Rate (AR)", formatDoubles(beatmaps, OsuBeatmap::getDifficultyApproach, performance), true);
            embed.addField("Aim Difficulty", formatDoubles(beatmaps, OsuBeatmap::getDifficultyAim, performance), true);
            embed.addField("Speed Difficulty", formatDoubles(beatmaps, OsuBeatmap::getDifficultySpeed, performance), true);
            embed.addField("Beatmap Statistics", "(weighted average, followed by the minimum, average and maximum values)", false);
            embed.addField("Length", formatSeconds(beatmaps, OsuBeatmap::getTotalLength, performance), true);
            embed.addField("Drain Length", formatSeconds(beatmaps, OsuBeatmap::getHitLength, performance), true);
            embed.addField("BPM", formatDoubles(beatmaps, OsuBeatmap::getBpm, performance), true);
            embed.addField("Circles", formatIntegers(beatmaps, OsuBeatmap::getCountNormal, performance), true);
            embed.addField("Sliders", formatIntegers(beatmaps, OsuBeatmap::getCountSlider, performance), true);
            embed.addField("Spinners", formatIntegers(beatmaps, OsuBeatmap::getCountSpinner, performance), true);
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

    private static <T> String formatDoubles(List<T> list, Function<T, Double> function, double[] performance) {
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        double performanceSum = 0;
        double performanceDivisor = 0;
        double weightedSum = 0;
        double weightedDivisor = 0;
        int count = 0;
        for (T e : list) {
            if (e != null) {
                Double value = function.apply(e);
                if (value != null) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                    performanceSum += performance[count] * value;
                    performanceDivisor += performance[count];
                    weightedSum += WEIGHT[count] * value;
                    weightedDivisor += WEIGHT[count];
                    count++;
                }
            }
        }
        if (count > 0) {
            double weightedAverage = weightedSum / weightedDivisor;
            double performanceAverage = performanceSum / performanceDivisor;
            return Formatter.formatDecimal(weightedAverage) + '\n' + Formatter.formatDecimal(min) + " - "
                    + Formatter.formatDecimal(performanceAverage) + " - " + Formatter.formatDecimal(max);
        } else {
            return "N/A";
        }
    }

    private static <T> String formatIntegers(List<T> list, Function<T, Integer> function, double[] performance) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        double performanceSum = 0;
        double performanceDivisor = 0;
        double weightedSum = 0;
        double weightedDivisor = 0;
        int count = 0;
        for (T e : list) {
            if (e != null) {
                Integer value = function.apply(e);
                if (value != null) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                    performanceSum += performance[count] * value;
                    performanceDivisor += performance[count];
                    weightedSum += WEIGHT[count] * value;
                    weightedDivisor += WEIGHT[count];
                    count++;
                }
            }
        }
        if (count > 0) {
            int weightedAverage = (int) Math.floor(weightedSum / weightedDivisor);
            int performanceAverage = (int) Math.floor(performanceSum / performanceDivisor);
            return Formatter.formatInteger(weightedAverage) + '\n' + Formatter.formatLargeInteger(min) + " - "
                    + Formatter.formatLargeInteger(performanceAverage) + " - " + Formatter.formatLargeInteger(max);
        } else {
            return "N/A";
        }
    }

    private static <T> String formatSeconds(List<T> list, Function<T, Integer> function, double[] performance) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        double performanceSum = 0;
        double performanceDivisor = 0;
        double weightedSum = 0;
        double weightedDivisor = 0;
        int count = 0;
        for (T e : list) {
            if (e != null) {
                Integer value = function.apply(e);
                if (value != null) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                    performanceSum += performance[count] * value;
                    performanceDivisor += performance[count];
                    weightedSum += WEIGHT[count] * value;
                    weightedDivisor += WEIGHT[count];
                    count++;
                }
            }
        }
        if (count > 0) {
            int weightedAverage = (int) Math.floor(weightedSum / weightedDivisor);
            int performanceAverage = (int) Math.floor(performanceSum / performanceDivisor);
            return Formatter.formatSeconds(weightedAverage) + '\n' + Formatter.formatSeconds(min) + " - "
                    + Formatter.formatSeconds(performanceAverage) + " - " + Formatter.formatSeconds(max);
        } else {
            return "N/A";
        }
    }

}
