package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.activity.ActivityGetMessageCommand;
import de.nevini.scope.Node;

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
