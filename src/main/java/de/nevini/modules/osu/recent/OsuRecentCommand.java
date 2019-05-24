package de.nevini.modules.osu.recent;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuScore;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.modules.Node;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.external.OsuModeResolver;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OsuRecentCommand extends Command {

    private static final StringResolver userResolver = new StringResolver("user", "user");
    private static final OsuModeResolver modeResolver = new OsuModeResolver();

    private final OsuService osu;

    public OsuRecentCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!recent")
                .guildOnly(false)
                .node(Node.OSU_STATS)
                .description("displays osu! user most recent plays")
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
        userResolver.resolveArgumentOrOptionOrInput(event, user -> acceptUser(event, user));
    }

    private void acceptUser(CommandEvent event, String user) {
        modeResolver.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, user, mode));
    }

    private void acceptUserAndMode(CommandEvent event, String input, GameMode mode) {
        List<OsuScore> scores = osu.getUserRecent(input, mode, 50);
        if (scores == null || scores.isEmpty()) {
            event.reply("No scores found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            event.getGameService().findGames("osu!").stream().findFirst().ifPresent(
                    game -> embed.setAuthor(game.getName(), null, game.getIcon())
            );
            int userId = scores.get(0).getUserID();
            String userName = osu.getUserName(userId);
            embed.setTitle(userName, "https://osu.ppy.sh/u/" + userId);
            for (OsuScore score : scores) {
                embed.addField(score.getRank() + " - " + Formatter.formatFloat(score.getPp()) + "pp - " + Formatter.formatLargestUnitAgo(score.getDate()),
                        "[" + osu.getBeatmapName(score.getBeatmapID()) + "](https://osu.ppy.sh/b/" + score.getBeatmapID() + ")",
                        false);
            }
            event.reply(embed, event::complete);
        }
    }

}
