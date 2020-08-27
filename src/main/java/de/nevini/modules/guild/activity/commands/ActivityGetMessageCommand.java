package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class ActivityGetMessageCommand extends Command {

    public ActivityGetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_ACTIVITY_GET)
                .description("displays user activity information for when they last posted a message "
                        + "in this Discord server")
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
        Long timestamp = event.getActivityService().getActivityMessage(member);
        if (timestamp == null) {
            event.reply("No message activity found for **" + member.getEffectiveName() + "**.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** (" + member.getUser().getId() + ") last message was **"
                    + Formatter.formatLargestUnitAgo(timestamp) + "** (" + Formatter.formatTimestamp(timestamp)
                    + ").", event::complete);
        }
    }

}
