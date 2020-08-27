package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.inactivity.commands.InactivityUnsetPlayingCommand;

class UnsetGameTimeoutCommand extends Command {

    private static final InactivityUnsetPlayingCommand delegate = new InactivityUnsetPlayingCommand();

    UnsetGameTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **inactivity unset playing**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
