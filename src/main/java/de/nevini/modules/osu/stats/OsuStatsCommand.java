package de.nevini.modules.osu.stats;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuUser;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.modules.Node;
import de.nevini.modules.osu.OsuModeResolver;
import de.nevini.resolvers.StringResolver;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OsuStatsCommand extends Command {

    private static final StringResolver USER_RESOLVER = new StringResolver("user", "user");
    private static final OsuModeResolver MODE_RESOLVER = new OsuModeResolver();

    private final OsuService osu;

    public OsuStatsCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!stats")
                .guildOnly(false)
                .node(Node.OSU_STATS)
                .description("Shows the entered user's osu! stats.")
                .options(new CommandOptionDescriptor[]{
                        CommandOptionDescriptor.builder()
                                .syntax("[--user] <user>")
                                .description("The osu! name of the user to look up. The flag is optional if this option is provided first.")
                                .keyword("--user")
                                .aliases(new String[]{"//user"})
                                .build(),
                        OsuModeResolver.describe().build()
                })
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        USER_RESOLVER.resolveArgumentOrOptionOrInput(event, user -> acceptUser(event, user));
    }

    private void acceptUser(CommandEvent event, String user) {
        MODE_RESOLVER.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, user, mode));
    }

    private void acceptUserAndMode(CommandEvent event, String input, GameMode mode) {
        OsuUser user = mode == null ? osu.getUser(input) : osu.getUser(input, mode);
        if (user == null) {
            event.reply("User not found", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            event.getGameService().findGames("osu!").stream().findFirst().ifPresent(
                    game -> embed.setAuthor(game.getName(), null, game.getIcon())
            );
            embed.setTitle(user.getUsername(), "https://osu.ppy.sh/u/" + user.getID());
            embed.setDescription(user.getCountry().getName());
            embed.addField("Game Mode", user.getMode().getName(), true);
            embed.addField("Global Ranking", "#" + Formatter.formatInteger(user.getRank()), true);
            embed.addField("Country Ranking", "#" + Formatter.formatInteger(user.getCountryRank()), true);
            embed.addField("Ranked Score", Formatter.formatLong(user.getRankedScore()), true);
            embed.addField("Hit Accuracy", Formatter.formatFloat(user.getAccuracy()) + '%', true);
            embed.addField("Play Count", Formatter.formatInteger(user.getPlayCount()), true);
            embed.addField("Total Score", Formatter.formatLong(user.getTotalScore()), true);
            embed.addField("Total Hits", Formatter.formatInteger(user.getTotalHits()), true);
            embed.addField("Performance Points", Formatter.formatInteger(user.getPP()), true);
            embed.addField("Level", Formatter.formatFloat(user.getLevel()), true);
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
