package de.nevini.modules.guild.activity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.resolvers.common.Resolvers;
import de.nevini.core.scope.Node;
import de.nevini.util.Formatter;
import de.nevini.util.command.CommandOptionDescriptor;
import net.dv8tion.jda.api.entities.Member;

public class ActivityGetAwayCommand extends Command {

    public ActivityGetAwayCommand() {
        super(CommandDescriptor.builder()
                .keyword("away")
                .node(Node.GUILD_ACTIVITY_GET)
                .description("displays user absence")
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
        Long timestamp = event.getActivityService().getActivityAway(member);
        if (timestamp == null) {
            event.reply("No absence found for **" + member.getEffectiveName() + "**.", event::complete);
        } else {
            event.reply("**" + member.getEffectiveName() + "** (" + member.getUser().getId()
                    + ") " + (timestamp > System.currentTimeMillis() ? "is" : "was") + " absent until "
                    + Formatter.formatTimestamp(timestamp), event::complete);
        }
    }

}
