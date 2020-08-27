package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;

import java.time.OffsetDateTime;

public class ActivitySetAwayCommand extends Command {

    public ActivitySetAwayCommand() {
        super(CommandDescriptor.builder()
                .keyword("away")
                .node(Node.GUILD_ACTIVITY_SET)
                .description("configures user absence")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true),
                        Resolvers.TIMESTAMP.describe()
                })
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
        if (timestamp.isBefore(OffsetDateTime.now())) {
            event.reply(CommandReaction.WARNING, "You cannot use a timestamp in the past!", event::complete);
            return;
        }

        // set activity
        event.getActivityService().manualActivityAway(member, timestamp);
        event.reply(CommandReaction.OK, event::complete);
    }

}
