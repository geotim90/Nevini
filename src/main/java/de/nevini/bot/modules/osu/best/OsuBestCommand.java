package de.nevini.bot.modules.osu.best;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUserBest;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuBestCommand extends Command {

    public OsuBestCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!best")
                .node(Node.OSU_BEST)
                .description("displays the top 100 scores of an osu! user")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        OsuResolvers.MODE.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrDefault(event,
                event.getMember(),
                member -> acceptUser(event, member));
    }

    private void acceptUser(CommandEvent event, Member member) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, member, mode));
    }

    private void acceptUserAndMode(CommandEvent event, Member member, OsuMode mode) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        List<OsuUserBest> best = osuService.getUserBest(ign, mode);
        if (best == null || best.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            int userId = best.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuUserBest score : best) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger((int) Math.floor(score.getPp())) + "pp - "
                                + Formatter.formatLargestUnitAgo(score.getDate().getTime()),
                        "[" + osuService.getBeatmapString(score.getBeatmapId())
                                + "](https://osu.ppy.sh/b/" + score.getBeatmapId() + ")",
                        false);
            }
            event.reply(embed, event::complete);
        }
    }

}
