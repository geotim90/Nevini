package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;

public class InactivityGetOnlineCommand extends Command {

    public InactivityGetOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("online")
                .aliases(new String[]{"last-online", "lastonline"})
                .node(Node.GUILD_INACTIVITY_GET)
                .description("displays the user inactivity threshold for when they were last online on Discord")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Integer days = event.getInactivityService().getMessageThreshold(event.getGuild());
        if (days == null || days < 1) {
            event.reply("No online timeout has been configured.", event::complete);
        } else {
            event.reply("The configured online timeout is **" + days.toString() + " days**", event::complete);
        }
    }

}
