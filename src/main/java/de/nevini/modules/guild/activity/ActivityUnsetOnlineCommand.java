package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ActivityUnsetOnlineCommand extends Command {

    public ActivityUnsetOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("online")
                .aliases(new String[]{"last-online", "lastonline"})
                .node(Node.GUILD_ACTIVITY_SET)
                .description("configures user activity information for when they were last online on Discord")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true)
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
        event.getActivityService().manualActivityOnline(member,
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneOffset.UTC));
        event.reply(CommandReaction.OK, event::complete);
    }

}
