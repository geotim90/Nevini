package de.nevini.modules.geobot.timeout;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.inactivity.InactivityGetOnlineCommand;
import de.nevini.scope.Node;

class GetTimeoutLastOnlineCommand extends Command {

    private static final InactivityGetOnlineCommand delegate = new InactivityGetOnlineCommand();

    GetTimeoutLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastonline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.GEOBOT_MOD)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **inactivity get online**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
