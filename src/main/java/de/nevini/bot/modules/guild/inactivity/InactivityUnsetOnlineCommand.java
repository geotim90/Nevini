package de.nevini.bot.modules.guild.inactivity;

import de.nevini.bot.command.Command;
import de.nevini.bot.command.CommandDescriptor;
import de.nevini.bot.command.CommandEvent;
import de.nevini.bot.scope.Node;
import de.nevini.framework.command.CommandReaction;

class InactivityUnsetOnlineCommand extends Command {

    InactivityUnsetOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("online")
                .aliases(new String[]{"last-online", "lastOnline"})
                .node(Node.GUILD_INACTIVITY_UNSET)
                .description("removes the user inactivity threshold for when they were last online on Discord")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getInactivityService().removeOnlineThreshold(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
