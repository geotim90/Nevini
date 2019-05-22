package de.nevini.modules.osu.stats;

import com.oopsjpeg.osu4j.OsuUser;
import de.nevini.command.*;
import de.nevini.db.game.GameData;
import de.nevini.modules.Node;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OsuStatsCommand extends Command {

    private final OsuService osu;

    public OsuStatsCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!stats")
                .node(Node.OSU_STATS)
                .description("Shows the entered user's stats.")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("<user>")
                                .description("The name of the user to look up.")
                                .keyword("")
                                .build()
                })
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        String input = event.getArgument();
        if (StringUtils.isEmpty(input)) {
            event.reply(CommandReaction.WARNING, "You did not provide a user!", event::complete);
        } else {
            OsuUser user = osu.getUser(input);
            if (user == null) {
                event.reply("User not found", event::complete);
            } else {
                EmbedBuilder embed = event.createEmbedBuilder();
                GameData game = event.getGameService().findGames("osu!").stream().findFirst().orElse(null);
                if (game != null) embed.setAuthor(game.getName(), null, game.getIcon());
                embed.setTitle(user.getUsername() + " - Global Ranking #" + Formatter.formatInteger(user.getRank()));
                embed.setDescription(user.getCountry().getName() + " - Country Ranking #" + Formatter.formatInteger(user.getCountryRank()));
                embed.addField("Game Mode", user.getMode().getName(), true);
                embed.addField("Ranked Score", Formatter.formatLong(user.getRankedScore()), true);
                embed.addField("Hit Accuracy", Formatter.formatAsPercent(user.getAccuracy()), true);
                embed.addField("Play Count", Formatter.formatInteger(user.getPlayCount()), true);
                embed.addField("Total Score", Formatter.formatLong(user.getTotalScore()), true);
                embed.addField("Total Hits", Formatter.formatInteger(user.getTotalHits()), true);
                embed.addField("Performance Points", Formatter.formatInteger(user.getPP()), true);
                embed.addField("SS+", Formatter.formatInteger(user.getCountRankSSH()), true);
                embed.addField("SS", Formatter.formatInteger(user.getCountRankSS()), true);
                embed.addField("S+", Formatter.formatInteger(user.getCountRankSH()), true);
                embed.addField("S", Formatter.formatInteger(user.getCountRankSS()), true);
                embed.addField("A", Formatter.formatInteger(user.getCountRankA()), true);
                embed.addField("300s", Formatter.formatInteger(user.getHit300()), true);
                embed.addField("100s", Formatter.formatInteger(user.getHit100()), true);
                embed.addField("50s", Formatter.formatInteger(user.getHit50()), true);
                event.reply(embed, event::complete);
            }
        }
    }

}
