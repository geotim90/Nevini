package de.nevini.bot.modules.guild.inactivity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.resolvers.StringResolver;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandOptionDescriptor;
import de.nevini.framework.command.CommandReaction;
import org.apache.commons.lang3.StringUtils;

class InactivitySetOnlineCommand extends Command {

    private static final StringResolver durationResolver = new StringResolver("duration", "duration",
            CommandOptionDescriptor.builder()
                    .syntax("[--duration] <days>")
                    .description("The number of days after which a user is considered inactive. The flag is optional.")
                    .keyword("--duration")
                    .aliases(new String[]{"//duration", "--days", "//days", "-d", "/d"})
                    .build());

    InactivitySetOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("online")
                .aliases(new String[]{"last-online", "lastOnline"})
                .node(Node.GUILD_INACTIVITY_SET)
                .description("configures the user inactivity threshold for when they were last online on Discord")
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
        // set inactivity threshold
        event.getInactivityService().setOnlineThreshold(event.getGuild(), days);
        event.reply(CommandReaction.OK, event::complete);
    }

}
