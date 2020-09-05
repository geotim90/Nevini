package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.activity.commands.ActivityUnsetOnlineCommand;

class UnsetMemberLastOnlineCommand extends Command {

    private static final ActivityUnsetOnlineCommand delegate = new ActivityUnsetOnlineCommand();

    UnsetMemberLastOnlineCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastonline")
                .aliases(new String[]{"last-online", "online"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity unset online**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
