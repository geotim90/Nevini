package de.nevini.modules.legacy.member;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.modules.Node;

public class SetMemberCommand extends Command {

    public SetMemberCommand() {
        super(CommandDescriptor.builder()
                .keyword("member")
                .node(Node.LEGACY_SET_MEMBER)
                .description("overwrites user activity information")
                .syntax("( (joined|lastOnline|lastMessage) <user> | lastPlayed <user> <game> ) <timestamp>")
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        // TODO
    }

}
