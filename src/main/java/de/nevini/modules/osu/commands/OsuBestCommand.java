package de.nevini.modules.osu.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.modules.osu.model.OsuMode;
import de.nevini.modules.osu.model.OsuUserBest;
import de.nevini.modules.osu.resolvers.OsuResolvers;
import de.nevini.modules.osu.resolvers.OsuUserResolver;
import de.nevini.modules.osu.services.OsuService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuBestCommand extends Command {

    public OsuBestCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!best")
                .guildOnly(false)
                .node(Node.OSU_BEST)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("displays the top 100 scores of an osu! user")
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
            EmbedBuilder embed = event.createGameEmbedBuilder(game);
            int userId = best.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuUserBest score : best) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger((int) Math.floor(score.getPp())) + "pp - "
                                + Formatter.formatLargestUnitAgo(score.getDate()),
                        "[" + osuService.getBeatmapString(score.getBeatmapId())
                                + "](https://osu.ppy.sh/b/" + score.getBeatmapId() + ")",
                        false);
            }
            event.notifyLongTaskEnd();
            event.reply(embed, event::complete);
        }
    }

}
