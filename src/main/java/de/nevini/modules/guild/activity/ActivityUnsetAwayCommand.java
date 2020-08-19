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

public class ActivityUnsetAwayCommand extends Command {

    public ActivityUnsetAwayCommand() {
        super(CommandDescriptor.builder()
                .keyword("away")
                .node(Node.GUILD_ACTIVITY_SET)
                .description("configures user absence")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        event.getActivityService().manualActivityAway(member,
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneOffset.UTC));
        event.reply(CommandReaction.OK, event::complete);
    }

}
