package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.StringResolver;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import net.dv8tion.jda.api.entities.Member;
import org.apache.commons.lang3.StringUtils;

public class TributeDelaySetCommand extends Command {

    private static final StringResolver durationResolver = new StringResolver("duration", "duration",
            CommandOptionDescriptor.builder()
                    .syntax("[--duration] <days>")
                    .description("The number of days for the delay or extension. "
                            + "The flag is optional if this option is provided first.")
                    .keyword("--duration")
                    .aliases(new String[]{"//duration", "--days", "//days", "-d", "/d"})
                    .build());

    public TributeDelaySetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_TRIBUTE_DELAY_SET)
                .description("configures delayed tribute timeouts for individual users")
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
        durationResolver.resolveOptionOrInput(event, duration -> acceptMemberDuration(event, member, duration));
    }

    private void acceptMemberDuration(CommandEvent event, Member member, String duration) {
        // validate duration
        int days;
        if (StringUtils.isEmpty(duration)) {
            // command was aborted or no input provided
            event.reply(CommandReaction.DEFAULT_NOK, event::complete);
            return;
        } else {
            try {
                // try to parse input
                days = Integer.parseInt(duration);
            } catch (NumberFormatException e) {
                // failed to parse duration
                event.reply(CommandReaction.WARNING, "You did not provide a valid duration!", event::complete);
                return;
            }
            if (days < 1) {
                event.reply(CommandReaction.WARNING, "The duration cannot be less than 1!", event::complete);
                return;
            }
        }
        // set tribute delay
        event.getTributeService().setDelay(member, days);
        event.reply(CommandReaction.OK, event::complete);
    }

}
