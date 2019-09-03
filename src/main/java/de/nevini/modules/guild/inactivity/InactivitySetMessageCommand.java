package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;

public class InactivitySetMessageCommand extends Command {

    public InactivitySetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_INACTIVITY_SET)
                .description("configures the user inactivity threshold for when they last posted a message "
                        + "in this Discord server")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.DURATION.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.DURATION.resolveArgumentOrOptionOrInput(event, duration -> acceptDuration(event, duration));
    }

    private void acceptDuration(CommandEvent event, Long duration) {
        event.getInactivityService().setMessageThreshold(event.getGuild(), duration.intValue());
        event.reply(CommandReaction.OK, event::complete);
    }

}
