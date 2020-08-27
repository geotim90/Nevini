package de.nevini.modules.osu.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.modules.osu.model.OsuMode;
import de.nevini.modules.osu.model.OsuUserRecent;
import de.nevini.modules.osu.resolvers.OsuResolvers;
import de.nevini.modules.osu.resolvers.OsuUserResolver;
import de.nevini.modules.osu.services.OsuService;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.EmbedBuilder;
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
                .guildOnly(false)
                .node(Node.OSU_RECENT)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("displays up to 50 most recent plays over the last 24 hours of an osu! user")
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
        List<OsuUserRecent> recent = osuService.getUserRecent(ign, mode);
        if (recent == null || recent.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            event.notifyLongTaskStart();
            EmbedBuilder embed = event.createGameEmbedBuilder(game);
            int userId = recent.get(0).getUserId();
            String userName = osuService.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuUserRecent score : recent) {
                embed.addField(Formatter.formatOsuRank(score.getRank()) + " "
                                + Formatter.formatInteger(score.getScore()) + " points - "
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
