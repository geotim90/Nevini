package de.nevini.modules.geobot.timeout;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.inactivity.commands.InactivitySetPlayingCommand;

class SetGameTimeoutCommand extends Command {

    private static final InactivitySetPlayingCommand delegate = new InactivitySetPlayingCommand();

    SetGameTimeoutCommand() {
        super(CommandDescriptor.builder()
                .keyword("timeout")
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **inactivity set playing**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
