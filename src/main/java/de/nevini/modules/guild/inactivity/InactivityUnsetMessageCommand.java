package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandReaction;

class InactivityUnsetMessageCommand extends Command {

    InactivityUnsetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_INACTIVITY_SET)
                .description("removes the user inactivity threshold for when they last posted a message "
                        + "in this Discord server")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getInactivityService().removeMessageThreshold(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
