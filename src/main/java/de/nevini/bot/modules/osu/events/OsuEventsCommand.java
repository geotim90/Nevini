package de.nevini.bot.modules.osu.events;

import com.oopsjpeg.osu4j.GameMode;
import com.oopsjpeg.osu4j.OsuUser;
import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.command.CommandOptionDescriptor;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.resolvers.external.OsuResolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.services.external.OsuService;
import de.nevini.bot.util.Formatter;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OsuEventsCommand extends Command {

    public OsuEventsCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!events")
                .children(new Command[]{
                        new OsuEventsFeedCommand()
                })
                .node(Node.OSU_EVENTS)
                .description("displays osu! user events")
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
        OsuResolvers.MODE.resolveOptionOrInputIfExists(event, mode -> acceptUserAndMode(event, member, mode));
    }

    private void acceptUserAndMode(CommandEvent event, Member member, GameMode mode) {
        OsuService osuService = event.locate(OsuService.class);
        GameData game = osuService.getGame();
        String ign = StringUtils.defaultIfEmpty(event.getIgnService().getIgn(member, game), member.getEffectiveName());
        OsuUser user = mode == null ? osuService.getUser(ign) : osuService.getUser(ign, mode, 31);
        if (user == null) {
            event.reply("User not found.", event::complete);
        } else if (user.getEvents().isEmpty()) {
            event.reply("No events found.", event::complete);
        } else {
            StringBuilder builder = new StringBuilder();
            for (OsuUser.Event e : user.getEvents()) {
                builder.append(Formatter.formatOsuDisplayHtml(e.getDisplayHTML())).append(" ")
                        .append(Formatter.formatLargestUnitAgo(e.getDate())).append('\n');
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}