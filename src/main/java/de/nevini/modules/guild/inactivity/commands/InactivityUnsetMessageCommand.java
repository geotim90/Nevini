package de.nevini.modules.guild.inactivity.commands;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.util.command.CommandReaction;

public class InactivityUnsetMessageCommand extends Command {

    public InactivityUnsetMessageCommand() {
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
