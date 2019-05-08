package de.nevini.modules.legacy.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class GetMemberCommand extends Command {

    public GetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .node(Node.LEGACY_GET_MEMBER)
                .description("displays user activity information")
                .syntax("( (joined|lastOnline|lastMessage) <user> | lastPlayed <user> <game> )")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
