package de.nevini.bot.modules.guild.activity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.db.game.GameData;
import de.nevini.bot.resolvers.StringResolver;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.bot.util.Formatter;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

class ActivitySetPlayingCommand extends Command {

    private static final StringResolver timestampResolver = new StringResolver("timestamp", "time", "t",
            CommandOptionDescriptor.builder()
                    .syntax("--time <timestamp>")
                    .description("A valid ISO 8601 UTC timestamp (e.g. `" + LocalDateTime.now().toString() + "`)."
                            + "\n`now` can be used as a shortcut for the current date and time.")
                    .keyword("--time")
                    .aliases(new String[]{"//time", "-t", "/t"})
                    .build());

    ActivitySetPlayingCommand() {
        super(CommandDescriptor.builder()
                .keyword("playing")
                .aliases(new String[]{"played", "last-played", "lastplayed"})
                .node(Node.GUILD_ACTIVITY_GET)
                .description("configures user game activity information")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.GAME.describe(),
                        timestampResolver.describe()
                })
                .details("Note that timestamps provided via this command do not override *real* activity information. "
                        + "Instead, they just provided a manual minimum value for activity reports on this server.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        Resolvers.GAME.resolveOptionOrInput(event, game -> acceptMemberGame(event, member, game));
    }

    private void acceptMemberGame(CommandEvent event, Member member, GameData game) {
        timestampResolver.resolveOptionOrInput(event,
                timestamp -> acceptMemberGameTimestamp(event, member, game, timestamp));
    }

    private void acceptMemberGameTimestamp(CommandEvent event, Member member, GameData game, String timestamp) {
        // validate timestamp
        OffsetDateTime dateTime;
        if (StringUtils.isEmpty(timestamp)) {
            // command was aborted or no input provided
            event.reply(CommandReaction.DEFAULT_NOK, event::complete);
            return;
        } else {
            try {
                // try to parse input
                dateTime = Formatter.parseTimestamp(timestamp);
            } catch (DateTimeParseException e) {
                // failed to parse timestamp
                event.reply(CommandReaction.WARNING, "You did not provide a valid timestamp!", event::complete);
                return;
            }
            if (dateTime.isAfter(OffsetDateTime.now())) {
                event.reply(CommandReaction.WARNING, "You cannot use a timestamp in future!", event::complete);
                return;
            }
        }
        // set activity
        event.getActivityService().manualActivityPlaying(member, game, dateTime);
        event.reply(CommandReaction.OK, event::complete);
    }

}
