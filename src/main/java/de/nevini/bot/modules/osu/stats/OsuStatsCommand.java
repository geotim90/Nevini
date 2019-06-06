package de.nevini.bot.modules.osu.stats;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.resolvers.osu.OsuResolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.services.osu.OsuService;
import de.nevini.bot.util.Formatter;
import de.nevini.framework.command.CommandOptionDescriptor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OsuStatsCommand extends Command {

    public OsuStatsCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!stats")
                .aliases(new String[]{"osu!user"})
                .node(Node.OSU_STATS)
                .description("displays general information of an osu! user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        OsuResolvers.MODE.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefault(
                event,
                event.getMember(),
                member -> acceptUser(event, member)
        );
    }

    private void acceptUser(CommandEvent event, Member member) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, member, mode));
    }

    private void acceptUserAndMode(CommandEvent event, Member member, OsuMode mode) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        OsuUser user = osuService.getUser(ign, mode);
        if (user == null) {
            event.reply("User not found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            embed.setTitle(user.getUserName(), "https://osu.ppy.sh/u/" + user.getUserId());

            embed.addField("Country", user.getCountry(), true);
            embed.addField("Game Mode", ObjectUtils.defaultIfNull(mode, OsuMode.STANDARD).getName(), true);
            embed.addField("Global Ranking", "#" + Formatter.formatInteger(user.getPpRank()), true);

            embed.addField("Performance Points", Formatter.formatInteger((int) Math.floor(user.getPpRaw())), true);
            embed.addField("Hit Accuracy", Formatter.formatFloat(user.getAccuracy()) + '%', true);
            embed.addField("Ranked Score", Formatter.formatLong(user.getRankedScore()), true);

            embed.addField("Country Ranking", "#" + Formatter.formatInteger(user.getPpCountryRank()), true);
            embed.addField("Level", Formatter.formatFloat(user.getLevel()), true);
            embed.addField("Total Score", Formatter.formatLong(user.getTotalScore()), true);

            embed.addField("Play Count", Formatter.formatInteger(user.getPlayCount()), true);
            embed.addField(Formatter.formatOsuRank("SS+"), Formatter.formatInteger(user.getCountRankSsh()), true);
            embed.addField(Formatter.formatOsuRank("SS"), Formatter.formatInteger(user.getCountRankSs()), true);

            embed.addField(Formatter.formatOsuRank("S+"), Formatter.formatInteger(user.getCountRankSh()), true);
            embed.addField(Formatter.formatOsuRank("S"), Formatter.formatInteger(user.getCountRankSs()), true);
            embed.addField(Formatter.formatOsuRank("A"), Formatter.formatInteger(user.getCountRankA()), true);

            embed.addField("300s", Formatter.formatInteger(user.getCount300()), true);
            embed.addField("100s", Formatter.formatInteger(user.getCount100()), true);
            embed.addField("50s", Formatter.formatInteger(user.getCount50()), true);

            event.reply(embed, event::complete);
        }
    }

}
