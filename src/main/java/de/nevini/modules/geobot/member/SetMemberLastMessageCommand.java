package de.nevini.modules.geobot.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.guild.activity.ActivitySetMessageCommand;
import de.nevini.scope.Node;

class SetMemberLastMessageCommand extends Command {

    private static final ActivitySetMessageCommand delegate = new ActivitySetMessageCommand();

    SetMemberLastMessageCommand() {
        super(CommandDescriptor.builder()
                .keyword("lastmessage")
                .aliases(new String[]{"last-message", "message"})
                .node(Node.GEOBOT_ADMIN)
                .description(delegate.getDescription())
                .options(delegate.getOptions())
                .details("This command will behave the same as **activity set message**.")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        delegate.onEvent(event);
    }

}
