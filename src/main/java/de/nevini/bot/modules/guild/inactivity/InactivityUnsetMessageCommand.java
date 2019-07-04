package de.nevini.bot.modules.guild.inactivity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandReaction;

class InactivityUnsetMessageCommand extends Command {

    InactivityUnsetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_INACTIVITY_UNSET)
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
