package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ActivityUnsetMessageCommand extends Command {

    public ActivityUnsetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_ACTIVITY_SET)
                .description("configures user activity information for when they last posted a message "
                        + "in this Discord server")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.TIMESTAMP.describe()
                })
                .details("Note that this command only removes timestamps provided via the **set** command "
                        + "and does not override *real* activity information.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        event.getActivityService().manualActivityMessage(member,
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneOffset.UTC));
        event.reply(CommandReaction.OK, event::complete);
    }

}
