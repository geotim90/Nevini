package de.nevini.modules.osu.stats;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
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
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Component
public class OsuStatsCommand extends Command {

    public OsuStatsCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!stats")
                .aliases(new String[]{"osu!user"})
                .guildOnly(false)
                .node(Node.OSU_STATS)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("displays general information of an osu! user")
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
                userOrMember -> acceptUserOrMember(event, userOrMember)
        );
    }

    private void acceptUserOrMember(CommandEvent event, OsuUserResolver.OsuUserOrMember userOrMember) {
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, userOrMember, mode));
    }

    private void acceptUserAndMode(CommandEvent event, OsuUserResolver.OsuUserOrMember userOrMember, OsuMode mode) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = OsuCommandUtils.resolveUserName(userOrMember, event.getIgnService(), game);
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
            embed.addField("Hit Accuracy", Formatter.formatDecimal(user.getAccuracy()) + '%', true);
            embed.addField("Ranked Score", Formatter.formatInteger(user.getRankedScore()), true);

            embed.addField("Country Ranking", "#" + Formatter.formatInteger(user.getPpCountryRank()), true);
            embed.addField("Level", Formatter.formatDecimal(user.getLevel()), true);
            embed.addField("Total Score", Formatter.formatInteger(user.getTotalScore()), true);

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
