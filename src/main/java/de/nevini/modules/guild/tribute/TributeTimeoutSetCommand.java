package de.nevini.modules.guild.tribute;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.StringResolver;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import org.apache.commons.lang3.StringUtils;

class TributeTimeoutSetCommand extends Command {

    private static final StringResolver durationResolver = new StringResolver("duration", "duration",
            CommandOptionDescriptor.builder()
                    .syntax("[--duration] <days>")
                    .description("The number of days in which a user must contribute. "
                            + "The flag is optional if this option is provided first.")
                    .keyword("--duration")
                    .aliases(new String[]{"//duration", "--days", "//days", "-d", "/d"})
                    .build());

    TributeTimeoutSetCommand() {
        super(CommandDescriptor.builder()
                .keyword("set")
                .node(Node.GUILD_TRIBUTE_TIMEOUT_SET)
                .description("configures the timeout for users that need to contribute")
                .options(new CommandOptionDescriptor[]{
                        durationResolver.describe()
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        durationResolver.resolveArgumentOrOptionOrInput(event, duration -> acceptDuration(event, duration));
    }

    private void acceptDuration(CommandEvent event, String duration) {
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
        // set tribute timeout
        event.getTributeService().setTimeout(event.getGuild(), days);
        event.reply(CommandReaction.OK, event::complete);
    }

}
