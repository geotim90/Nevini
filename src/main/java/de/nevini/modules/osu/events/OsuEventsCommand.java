package de.nevini.modules.osu.events;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuUser;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OsuEventsCommand extends Command {

    private static final StringResolver userResolver = new StringResolver("user", "user");
    private static final OsuModeResolver modeResolver = new OsuModeResolver();

    private final OsuService osu;

    public OsuEventsCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!events")
                .guildOnly(false)
                .node(Node.OSU_STATS)
                .description("displays osu! user events")
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
        OsuUser user = mode == null ? osu.getUser(input) : osu.getUser(input, mode, 31);
        if (user == null) {
            event.reply("User not found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            event.getGameService().findGames("osu!").stream().findFirst().ifPresent(
                    game -> embed.setAuthor(game.getName(), null, game.getIcon())
            );
            embed.setTitle(user.getUsername(), "https://osu.ppy.sh/u/" + user.getID());
            embed.setDescription(user.getCountry().getName());
            for (OsuUser.Event e : user.getEvents()) {
                String markdown = convertHtmlToMarkdown(e.getDisplayHTML());
                if (StringUtils.isNotEmpty(markdown)) {
                    embed.addField(Formatter.formatTimestamp(e.getDate()), markdown, false);
                }
            }
            event.reply(embed, event::complete);
        }
    }

    private String convertHtmlToMarkdown(String html) {
        return html.replaceAll("<img src='/images/(\\w+)_small.png'/>", "**$1**") // resolve rank images
                .replaceAll("<b><a href='(/u/\\d+)'>([^<]+)</a></b>", "[$2](https://osu.ppy.sh$1)") // resolve user references
                .replaceAll("<a href='(/b/\\d+\\?m=\\d)'>([^<]+)</a>", "[$2](https://osu.ppy.sh$1)"); // resolve user references
    }

}
