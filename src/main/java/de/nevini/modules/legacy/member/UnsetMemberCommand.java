package de.nevini.modules.legacy.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class UnsetMemberCommand extends Command {

    public UnsetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .node(Node.LEGACY_UNSET_MEMBER)
                .description("resets user activity information")
                .syntax("( (joined|lastOnline|lastMessage) <user> | lastPlayed <user> <game> )")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
