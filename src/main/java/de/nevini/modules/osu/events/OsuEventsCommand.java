package de.nevini.modules.osu.events;

import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuUser;
import de.nevini.api.osu.model.OsuUserEvent;
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
import org.springframework.stereotype.Component;

@Component
public class OsuEventsCommand extends Command {

    public OsuEventsCommand() {
        super(CommandDescriptor.builder()
                .keyword("osu!events")
                .children(new Command[]{
                        new OsuEventsFeedCommand()
                })
                .guildOnly(false)
                .node(Node.OSU_EVENTS)
                .minimumBotPermissions(Permissions.BOT_EMBED_EXT)
                .description("displays osu! user events")
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
        OsuUser user = osuService.getUserEvents(ign, mode, 31);
        if (user == null) {
            event.reply("User not found.", event::complete);
        } else if (user.getEvents().isEmpty()) {
            event.reply("No events found.", event::complete);
        } else {
            StringBuilder builder = new StringBuilder();
            for (OsuUserEvent e : user.getEvents()) {
                builder.append(Formatter.formatOsuDisplayHtml(e.getDisplayHtml())).append(" ")
                        .append(Formatter.formatLargestUnitAgo(e.getDate().getTime())).append('\n');
            }
            event.reply(builder.toString(), event::complete);
        }
    }

}
