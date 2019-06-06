package de.nevini.bot.modules.osu.recent;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUserRecent;
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
public class OsuRecentCommand extends Command {

    public OsuRecentCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!recent")
                .aliases(new String[]{"osu!recents"})
                .children(new Command[]{
                        new OsuRecentFeedCommand()
                })
                .node(Node.OSU_RECENT)
                .description("displays up to 50 most recent plays over the last 24 hours of an osu! user")
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
                member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptMemberAndMode(event, member, mode));
    }

    private void acceptMemberAndMode(CommandEvent event, Member member, OsuMode mode) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        List<OsuUserRecent> recent = osuService.getUserRecent(ign, mode);
        if (recent == null || recent.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            int userId = recent.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuUserRecent score : recent) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger(score.getScore()) + " points - "
                                + Formatter.formatLargestUnitAgo(score.getDate().getTime()),
                        "[" + osuService.getBeatmapString(score.getBeatmapId())
                                + "](https://osu.ppy.sh/b/" + score.getBeatmapId() + ")",
                        false);
            }
            event.reply(embed, event::complete);
        }
    }

}
