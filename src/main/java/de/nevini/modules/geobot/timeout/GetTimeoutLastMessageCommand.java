package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.inactivity.commands.InactivityGetMessageCommand;

class GetTimeoutLastMessageCommand extends Command {

    private static final InactivityGetMessageCommand delegate = new InactivityGetMessageCommand();

    GetTimeoutLastMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastmessage")
                .aliases(new String[]{"last-message", "message"})
                .node(Node.GEOBOT_MOD)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **inactivity get message**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
