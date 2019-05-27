package de.nevini.modules.osu.events;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuUser;
import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.command.CommandOptionDescriptor;
import de.nevini.db.game.GameData;
import de.nevini.resolvers.common.MemberResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.resolvers.external.OsuModeResolver;
import de.nevini.scope.Node;
import de.nevini.services.external.OsuService;
import de.nevini.util.Formatter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OsuEventsCommand extends Command {

    private static final OsuModeResolver modeResolver = new OsuModeResolver();

    private final OsuService osu;

    public OsuEventsCommand(@Autowired OsuService osu) {
        super(CommandDescriptor.builder()
                .keyword("osu!events")
                .aliases(new String[]{"osu!event"})
                .children(new Command[]{
                        new OsuEventsFeedCommand()
                })
                .node(Node.OSU_STATS)
                .description("displays osu! user events")
                .options(new CommandOptionDescriptor[]{
                        MemberResolver.describe().build(),
                        OsuModeResolver.describe().build()
                })
                .build());
        this.osu = osu;
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        modeResolver.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, member, mode));
    }

    private void acceptUserAndMode(CommandEvent event, Member member, GameMode mode) {
        GameData game = osu.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        OsuUser user = mode == null ? osu.getUser(ign) : osu.getUser(ign, mode, 31);
        if (user == null) {
            event.reply("User not found.", event::complete);
        } else if (user.getEvents().isEmpty()) {
            event.reply("No events found.", event::complete);
        } else {
            EmbedBuilder embed = event.createEmbedBuilder();
            embed.setAuthor(game.getName(), null, game.getIcon());
            embed.setTitle(user.getUsername(), "https://osu.ppy.sh/u/" + user.getID());
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
                .replaceAll("<a href='(/b/\\d+\\?m=\\d)'>([^<]+)</a>", "[$2](https://osu.ppy.sh$1)") // resolve beatmap references
                // resolve HTML formatting
                .replaceAll("<b>([^<]+)</b>", "**$1**") // bold text emphasis
                // resolve HTML entities
                .replaceAll("&amp;", "&")
                .replaceAll("&gt;", ">")
                .replaceAll("&lt;", "<");
    }

}
