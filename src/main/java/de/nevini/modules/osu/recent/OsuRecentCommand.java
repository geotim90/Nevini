package de.nevini.modules.osu.recent;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUserRecent;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.jpa.game.GameData;
import de.nevini.modules.osu.OsuCommandUtils;
import de.nevini.resolvers.osu.OsuResolvers;
import de.nevini.resolvers.osu.OsuUserResolver;
import de.nevini.scope.Node;
import de.nevini.scope.Permissions;
import de.nevini.services.osu.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
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
            event.notifyLongTaskEnd();
            event.reply(embed, event::complete);
        }
    }

}