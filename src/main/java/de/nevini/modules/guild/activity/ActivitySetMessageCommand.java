package de.nevini.modules.guild.activity;

import de.nevini.command.*;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.core.entities.Member;

import java.time.OffsetDateTime;

class ActivitySetMessageCommand extends Command {

    ActivitySetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_ACTIVITY_GET)
                .description("configures user activity information for when they last posted a message "
                        + "in this Discord server")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.TIMESTAMP.describe()
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
        Resolvers.TIMESTAMP.resolveOptionOrInput(event,
                timestamp -> acceptMemberAndTimestamp(event, member, timestamp));
    }

    private void acceptMemberAndTimestamp(CommandEvent event, Member member, OffsetDateTime timestamp) {
        // validate timestamp
        if (timestamp.isAfter(OffsetDateTime.now())) {
            event.reply(CommandReaction.WARNING, "You cannot use a timestamp in future!", event::complete);
            return;
        }

        // set activity
        event.getActivityService().manualActivityMessage(member, timestamp);
        event.reply(CommandReaction.OK, event::complete);
    }

}
