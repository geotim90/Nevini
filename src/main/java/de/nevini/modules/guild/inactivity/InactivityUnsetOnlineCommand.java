package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandReaction;

public class InactivityUnsetOnlineCommand extends Command {

    public InactivityUnsetOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("online")
                .aliases(new String[]{"last-online", "lastonline"})
                .node(Node.GUILD_INACTIVITY_SET)
                .description("removes the user inactivity threshold for when they were last online on Discord")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getInactivityService().removeOnlineThreshold(event.getGuild());
        event.reply(CommandReaction.OK, event::complete);
    }

}
