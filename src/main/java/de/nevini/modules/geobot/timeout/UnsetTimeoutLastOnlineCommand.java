package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.inactivity.commands.InactivityUnsetOnlineCommand;

class UnsetTimeoutLastOnlineCommand extends Command {

    private static final InactivityUnsetOnlineCommand delegate = new InactivityUnsetOnlineCommand();

    UnsetTimeoutLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastonline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **inactivity unset online**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
