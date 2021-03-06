package de.nevini.modules.osu.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.core.scope.Permissions;
import de.nevini.modules.admin.game.data.GameData;
import de.nevini.modules.osu.model.OsuMode;
import de.nevini.modules.osu.model.OsuUser;
import de.nevini.modules.osu.resolvers.OsuResolvers;
import de.nevini.modules.osu.resolvers.OsuUserResolver;
import de.nevini.modules.osu.services.OsuService;
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
                .aliases(new String[]{"osu!user", "osu!profile"})
                .guildOnly(false)
                .node(Node.OSU)
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
            EmbedBuilder embed = event.createGameEmbedBuilder(game);
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
