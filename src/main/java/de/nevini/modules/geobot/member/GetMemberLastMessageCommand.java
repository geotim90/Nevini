package de.nevini.modules.geobot.member;

import de.nevini.core.command.Command;
import de.nevini.core.command.CommandDescriptor;
import de.nevini.core.command.CommandEvent;
import de.nevini.core.scope.Node;
import de.nevini.modules.guild.activity.commands.ActivityGetMessageCommand;

class GetMemberLastMessageCommand extends Command {

    private static final ActivityGetMessageCommand delegate = new ActivityGetMessageCommand();

    GetMemberLastMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastmessage")
                .aliases(new String[]{"last-message", "message"})
                .node(Node.GEOBOT_MOD)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity get message**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
