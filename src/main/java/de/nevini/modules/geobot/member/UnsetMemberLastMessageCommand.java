package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.activity.commands.ActivityUnsetMessageCommand;

class UnsetMemberLastMessageCommand extends Command {

    private static final ActivityUnsetMessageCommand delegate = new ActivityUnsetMessageCommand();

    UnsetMemberLastMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastmessage")
                .aliases(new String[]{"last-message", "message"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity unset message**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
