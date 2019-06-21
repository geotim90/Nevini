package de.nevini.bot.modules.guild.activity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.StringResolver;
import de.nevini.bot.resolvers.common.Resolvers;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import net.dv8tion.jda.core.entities.Member;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

class ActivitySetMessageCommand extends Command {

    private static final StringResolver timestampResolver = new StringResolver("timestamp", "time",
            CommandOptionDescriptor.builder()
                    .syntax("--time <timestamp>")
                    .description("A valid ISO 8601 UTC timestamp (e.g. `" + LocalDateTime.now().toString() + "`)."
                            + "\n`now` can be used as a shortcut for the current date and time.")
                    .keyword("--time")
                    .aliases(new String[]{"//time", "-t", "/t"})
                    .build());

    ActivitySetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastMessage"})
                .node(Node.GUILD_ACTIVITY_GET)
                .description("configures user activity information for when they last posted a message "
                        + "in this Discord server")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
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
        timestampResolver.resolveOptionOrInput(event,
                timestamp -> acceptMemberAndTimestamp(event, member, timestamp));
    }

    private void acceptMemberAndTimestamp(CommandEvent event, Member member, String timestamp) {
        // validate timestamp
        OffsetDateTime dateTime;
        if (StringUtils.isEmpty(timestamp)) {
            // command was aborted or no input provided
            event.reply(CommandReaction.DEFAULT_NOK, event::complete);
            return;
        } else if (timestamp.equalsIgnoreCase("now")) {
            // get "now"
            dateTime = OffsetDateTime.now(ZoneOffset.UTC);
        } else {
            try {
                // try to parse input
                dateTime = LocalDateTime.parse(timestamp).atOffset(ZoneOffset.UTC);
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
        event.getActivityService().manualActivityMessage(member, dateTime);
        event.reply(CommandReaction.OK, event::complete);
    }

}
