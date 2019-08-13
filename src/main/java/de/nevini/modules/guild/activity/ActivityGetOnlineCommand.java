package de.nevini.modules.guild.activity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class ActivityGetOnlineCommand extends Command {

    public ActivityGetOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("online")
                .aliases(new String[]{"last-online", "lastonline"})
                .node(Node.GUILD_ACTIVITY_GET)
                .description("displays user activity information for when they were last online on Discord")
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
        Long timestamp = event.getActivityService().getActivityOnline(member);
        if (timestamp == null) {
            event.reply("No online status found for **" + member.getEffectiveName() + "**.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** (" + member.getUser().getId() + ") was last online **"
                    + Formatter.formatLargestUnitAgo(timestamp) + "** (" + Formatter.formatTimestamp(timestamp)
                    + ").", event::complete);
        }
    }

}
