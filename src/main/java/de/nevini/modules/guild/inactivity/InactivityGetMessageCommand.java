package de.nevini.modules.guild.inactivity;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.scope.Node;

public class InactivityGetMessageCommand extends Command {

    public InactivityGetMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("message")
                .aliases(new String[]{"last-message", "lastmessage"})
                .node(Node.GUILD_INACTIVITY_GET)
                .description("displays the user inactivity threshold for when they last posted a message "
                        + "in this Discord server")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Integer days = event.getInactivityService().getMessageThreshold(event.getGuild());
        if (days == null || days < 1) {
            event.reply("No message timeout has been configured.", event::complete);
        } else {
            event.reply("The configured message timeout is **" + days.toString() + " days**", event::complete);
        }
    }

}
