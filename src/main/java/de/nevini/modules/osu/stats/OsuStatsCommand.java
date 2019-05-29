package de.nevini.modules.osu.stats;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuUser;
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

@Component
public class OsuStatsCommand extends Command {

    private static final OsuModeResolver modeResolver = new OsuModeResolver();

    private final OsuService osu;

    public OsuStatsCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!stats")
                .aliases(new String[]{"osu!user"})
                .node(Node.OSU_STATS)
                .description("displays general information of an osu! user")
                .options(new CommandOptionDescriptor[]{
                        MemberResolver.describe().build(),
                        OsuModeResolver.describe().build()
                })
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefault(event, event.getMember(), member -> acceptUser(event, member));
    }

    private void acceptUser(CommandEvent event, Member member) {
        modeResolver.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, member, mode));
    }

    private void acceptUserAndMode(CommandEvent event, Member member, GameMode mode) {
        GameData game = osu.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        OsuUser user = osu.getUser(ign, mode);
        if (user == null) {
            event.reply("User not found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
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
            embed.addField(Formatter.formatOsuRank("SS+"), Formatter.formatInteger(user.getCountRankSSH()), true);
            embed.addField(Formatter.formatOsuRank("SS"), Formatter.formatInteger(user.getCountRankSS()), true);
            embed.addField(Formatter.formatOsuRank("S+"), Formatter.formatInteger(user.getCountRankSH()), true);
            embed.addField(Formatter.formatOsuRank("S"), Formatter.formatInteger(user.getCountRankSS()), true);
            embed.addField(Formatter.formatOsuRank("A"), Formatter.formatInteger(user.getCountRankA()), true);
            embed.addField("300s", Formatter.formatInteger(user.getHit300()), true);
            embed.addField("100s", Formatter.formatInteger(user.getHit100()), true);
            embed.addField("50s", Formatter.formatInteger(user.getHit50()), true);
            event.reply(embed, event::complete);
        }
    }

}
